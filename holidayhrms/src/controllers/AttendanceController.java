package controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import DAO_Interfaces.EmployeeAttendanceDAO;
import DAO_Interfaces.EmployeeDAO;
import models.AttendanceEvent;
import models.AttendanceRequest;
import models.Employee;
import models.EmployeeAttendance;
import models.EmployeeAttendanceId;
import models.EmployeeRequestResult;
import service_interfaces.EmployeeAttendanceServiceInterface;

@Controller
public class AttendanceController {

	private static Logger logger = LoggerFactory.getLogger(AttendanceController.class);

	private EmployeeAttendance attendance;
	private EmployeeAttendanceId attendanceId;
	private Gson gson;
	private EmployeeDAO employeeDAO;
	private EmployeeAttendanceServiceInterface employeeAttendanceService;
	private EmployeeAttendanceDAO employeeAttendanceDAO;

	@Autowired
	public AttendanceController(EmployeeAttendance attendance, EmployeeAttendanceId attendanceId, Gson gson,
			EmployeeDAO employeeDAO, EmployeeAttendanceDAO employeeAttendanceDAO,
			EmployeeAttendanceServiceInterface employeeAttendanceService) {
		this.attendance = attendance;
		this.attendanceId = attendanceId;
		this.gson = gson;
		this.employeeDAO = employeeDAO;
		this.employeeAttendanceService = employeeAttendanceService;
		this.employeeAttendanceDAO = employeeAttendanceDAO;
	}

	// gets the attendance form to upload the attendance
	@RequestMapping(value = "/attendanceform", method = RequestMethod.GET)
	public String uploadAttendanceForm() {
		logger.info("Accessing /attendanceform endpoint");
		return "attendanceform";
	}

	// gets the attendance form the employee
	@RequestMapping(value = "/employeeAttendance", method = RequestMethod.GET)
	public String employeeAttendanceForm(HttpSession session, Model model) {

		try {

			int id = (int) session.getAttribute("employeeId");
			// Retrieves the employee ID from the session

			logger.info("Accessing /employeeAttendance endpoint for employee ID: {}", id);

			Employee employee = employeeDAO.getEmployee(id);
			// Retrieves the employee information from the DAO using the employee ID

			if (employee == null) {
				logger.error("Employee not found for employee ID: {}", id);
				return "error";
			}
			Date joinDate = employee.getEmplJondate();
			// Retrieves the join date of the employee

			List<Integer> yearsList = employeeAttendanceService.getYears(joinDate);
			// Retrieves a list of years based on the employee's join date using the service method

			model.addAttribute("years", yearsList);
			// Adds the yearsList as an attribute named "years" to the model

			logger.info("Employee attendance form loaded successfully for employee ID: {}", id);

			return "employeeAttendance";
			// Returns the view name "employeeAttendance" to render the employee attendance form
		} catch (Exception e) {
			logger.error("An error occurred while accessing /employeeAttendance endpoint", e);
			return "error";
		}

	}

	// gets Admin side attendance form
	@RequestMapping(value = "/adminAttendance", method = RequestMethod.GET)
	public String adminAttendanceForm() {

		logger.info("Accessing /adminAttendance endpoint");
		return "adminAttendance";
	}

	// to get attendance data
	@RequestMapping(value = "/attendance", method = RequestMethod.POST)
	public ResponseEntity<String> attendanceData(@ModelAttribute AttendanceRequest attendanceRequest) {
		try {
			int year = attendanceRequest.getYear();
			int month = attendanceRequest.getMonth();
			int id = attendanceRequest.getEmployeeid();
			// Retrieves the year, month, and employee ID from the attendance request object

			logger.info("Processing attendance data for year: {}, month: {}, employee ID: {}", year, month, id);

			List<Object[]> results = employeeAttendanceDAO.getPunchInAndPunchOutDataForYearAndMonthAndEmployee(id, year,
					month);
			// Retrieves the punch in and punch out data for the specified year, month, and employee ID from the DAO

			if (results == null || results.isEmpty()) {

				logger.warn("No attendance data found for year: {}, month: {}, employee ID: {}", year, month, id);

				// Handle case where no attendance data is found
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No attendance data found.");
			}

			EmployeeRequestResult response = employeeAttendanceService.calculateAttendance(results);
			// Calls the service method to calculate the attendance based on the retrieved results

			logger.info("Attendance calculated for year: {}, month: {}, employee ID: {}", year, month, id);

			return ResponseEntity.ok(gson.toJson(response));
			// Returns a response entity with the calculated attendance result serialized as JSON

		} catch (Exception e) {
			logger.error("An error occurred while processing attendance data", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
		}
	}

	// to get punch data for graphs
	@RequestMapping(value = "/punchData", method = RequestMethod.GET)
	public ResponseEntity<String> getPunchData(HttpSession session) {
		try {
			int id = (int) session.getAttribute("employeeId");
			// Gets the employee ID from the session

			logger.info("Getting punch data for employee ID: {}", id);
			List<AttendanceEvent> punchData = employeeAttendanceService.getYesterdayPunchData(id);
			// Retrieves yesterday's punch-in and punch-out data for the employee ID

			if (punchData == null || punchData.isEmpty()) {
				// Handle case where no punch data is found
				logger.warn("No punch data found for yesterday for employee ID: {}", id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No punch data found for yesterday.");
			}
			logger.info("Punch data retrieved for employee ID: {}", id);
			return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(punchData));
			// Returns a response entity with the punch data serialized as JSON
		} catch (Exception e) {
			// Handle any other exceptions that may occur
			logger.error("An error occurred while getting punch data", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
		}
	}

	// uploads the attendance from excel to the database
	@RequestMapping(value = "/uploadAttendance", method = RequestMethod.POST)
	public ResponseEntity<String> uploadEmployeeAttendance(@RequestParam("file") MultipartFile file) {

		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {

			// Get the first sheet from the workbook
			Sheet sheet = workbook.getSheetAt(0);

			// Create an iterator to iterate over the rows of the sheet
			Iterator<Row> rowIterator = sheet.iterator();

			rowIterator.next(); // Skip header row

			// iterate over each row
			while (rowIterator.hasNext()) {

				// get next row
				Row row = rowIterator.next();

				// Assuming column order: employeeId, punchIn, punchOut, punchSystem
				// retrieve respective data from the excel
				Integer employeeId = (int) row.getCell(0).getNumericCellValue();
				LocalDateTime punchIn = employeeAttendanceService.convertToDateTime(row.getCell(1));
				LocalDateTime punchOut = employeeAttendanceService.convertToDateTime(row.getCell(2));
				String punchSystem = row.getCell(3).toString();

				logger.info("Processing attendance record for employee ID: {}", employeeId);

				// set the data to the model
				attendance.setPunchIn(punchIn);
				attendance.setPunchOut(punchOut);
				attendance.setPunchSystem(punchSystem);
				attendanceId.setEmployeeId(employeeId);

				// generate next index for the record
				int nextIndex = employeeAttendanceDAO.getNextAttendanceRequestIndex(employeeId);
				attendanceId.setEmplPIndex(nextIndex);
				attendance.setAttendanceId(attendanceId);

				// insert the record into database using service
				employeeAttendanceService.insertEmployeeAttendance(attendance);

				logger.info("Attendance record processed for employee ID: {}", employeeId);

			}
		} catch (IOException e) {
			// print the stack trace of IO Exception
			logger.error("An error occurred while uploading Employee Attendance data", e);
			String errorMessage = "File Parsing Error";
			// return 500 error for any internal errors
			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// on successful completion return 200 status with success message
		return ResponseEntity.ok("succesfully updated");
	}

	// gets the list of years starting from the employee join date
	@RequestMapping(value = "/getYearsList", method = RequestMethod.POST)
	public ResponseEntity<String> getYearsOfEmployee(@ModelAttribute AttendanceRequest attendanceRequest) {
		try {
			int id = attendanceRequest.getEmployeeid();
			// Retrieves the employee ID from the attendance request object

			logger.info("Getting years of employee with ID: {}", id);

			Employee employee = employeeDAO.getEmployee(id);
			// Retrieves the employee information from the DAO using the employee ID

			if (employee == null) {
				// Handle case where employee is not found
				logger.warn("Employee not found with ID: {}", id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
			}

			Date joinDate = employee.getEmplJondate();
			// Retrieves the join date of the employee

			List<Integer> yearsList = employeeAttendanceService.getYears(joinDate);
			// Retrieves a list of years based on the employee's join date using the service method

			logger.info("Years of employee retrieved successfully for employee ID: {}", id);

			return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(yearsList));
			// Returns a response entity with the yearsList serialized as JSON and an HTTP status code indicating a
			// successful request
		} catch (Exception e) {
			// Handle any other exceptions that may occur
			logger.error("An error occurred while getting list of years for an employee", e);
			String errorMessage = "Internal Server Error";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@RequestMapping(value = "/getAvgPunchInAndOut", method = RequestMethod.GET)
	public ResponseEntity<String> getAvgPunchInAndOut(HttpSession session) {
		try {
			int id = (int) session.getAttribute("employeeId");
			// Retrieves the employee ID from the session

			logger.info("Getting average punch in and punch out time for employee with ID: {}", id);

			List<Long> result = employeeAttendanceService.getAvgPunchInAndOut(id);
			// Retrieves the average punch in and punch out time for the employee ID using the service method

			logger.info("Average punch in and punch out time retrieved successfully for employee ID: {}", id);

			return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(result));
			// Returns a response entity with the average punch in and punch out time serialized as JSON and an HTTP
			// status code indicating a successful request
		} catch (Exception e) {
			// Handle any exceptions that may occur
			logger.error("An error occured while getting average punchin and punchout data for an employee", e);
			String errorMessage = "Internal Server Error";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

}