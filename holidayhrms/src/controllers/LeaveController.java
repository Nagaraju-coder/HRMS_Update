package controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

import DAO_Interfaces.EmployeeDAO;
import DAO_Interfaces.EmployeeLeaveRequestDAO;
import DAO_Interfaces.HolidayDAO;
import models.ApprovedLeaveModel;
import models.Employee;
import models.EmployeeLeaveInputModel;
import models.EmployeeLeaveModel;
import models.EmployeeLeaveRequest;
import models.EmployeeLeaveRequestId;
import models.HrmsJobGrade;
import models.JobGradeWiseLeaves;
import models.LeaveValidationModel;
import models.input.output.JobGradeLeavesOutModel;
import service.EmployeeLeaveService;
import service_interfaces.EmployeeLeaveServiceInterface;

@Controller
public class LeaveController {

	private static Logger logger = LoggerFactory.getLogger(LeaveController.class);

	private EmployeeLeaveRequest leaveRequest;
	private EmployeeLeaveRequestId leaveRequestId;
	private Gson gson;
	private EmployeeLeaveServiceInterface employeeService;
	private EmployeeLeaveRequestDAO leaveRequestDAO;
	private HolidayDAO holidayDAO;
	private JobGradeWiseLeaves jobGradeWiseLeaves;

	@Autowired
	public LeaveController(EmployeeLeaveRequest leaveRequest, EmployeeLeaveRequestId leaveRequestId, Gson gson,
			EmployeeLeaveService employeeService, EmployeeLeaveRequestDAO leaveRequestDAO, HolidayDAO holidayDAO,
			JobGradeWiseLeaves jobGradeWiseLeaves) {
		this.leaveRequest = leaveRequest;
		this.leaveRequestId = leaveRequestId;
		this.gson = gson;
		this.employeeService = employeeService;
		this.leaveRequestDAO = leaveRequestDAO;
		this.holidayDAO = holidayDAO;
		this.jobGradeWiseLeaves = jobGradeWiseLeaves;
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private EmployeeDAO employeeDAO;

	@Autowired
	private ApplicationContext context;

	// to get the leave form
	@RequestMapping(value = "/leaveform", method = RequestMethod.GET)
	public String applyLeaveForm(HttpSession session, Model model) {

		try {

			int id = (int) session.getAttribute("employeeId");
			// Retrieves the employee ID from the session

			Employee employee = employeeDAO.getEmployee(id);
			// Retrieves the employee information from the DAO using the employee ID

			JobGradeWiseLeaves leavesProvidedStatistics = leaveRequestDAO
					.getJobGradeWiseLeaves(employee.getEmplJbgrId().trim());
			// Retrieves the leaves provided statistics based on the job grade ID of the
			// employee from the DAO

			List<EmployeeLeaveRequest> leaves = leaveRequestDAO
					.getApprovedAndPendingEmployeeAndLeaveRequestData(employee.getEmplId(), Year.now().getValue());
			// Retrieves the approved and pending leave request data for the employee ID and
			// the current year from the DAO

			LeaveValidationModel validation = employeeService.calculateLeavesTaken(leaves, leavesProvidedStatistics);
			// Calculates the leaves taken based on the retrieved leave request data and
			// leaves provided statistics using
			// the service method

			model.addAttribute("validationData", validation);
			// Adds the validation data as an attribute named "validationData" to the model

			logger.info("Requested the Leave form");

			return "leaveform";
		} catch (Exception e) {
			logger.error("Failed to process leave request form.", e);
			return "error";
		}
	}

	// to submit leave
	@Transactional
	@RequestMapping(value = "/submitleave", method = RequestMethod.POST)
	public ResponseEntity<String> submitLeaveRequest(@ModelAttribute EmployeeLeaveInputModel employeeLeaveInputModel) {

		try {

			// Set the leave request properties from the input model
			leaveRequest.setLeaveStartDate(
					LocalDate.parse(employeeLeaveInputModel.getLeaveStartDate(), DateTimeFormatter.ISO_DATE));
			leaveRequest.setLeaveEndDate(
					LocalDate.parse(employeeLeaveInputModel.getLeaveEndDate(), DateTimeFormatter.ISO_DATE));
			leaveRequest.setLeaveType(employeeLeaveInputModel.getLeaveType());
			leaveRequest.setReason(employeeLeaveInputModel.getReason());
			leaveRequest.setRequestDateTime(LocalDateTime.now());
			leaveRequestId.setEmployeeId(employeeLeaveInputModel.getEmployeeId());

			// Generate the next leave request index
			int nextLeaveRequestIndex = leaveRequestDAO
					.getNextLeaveRequestIndex(employeeLeaveInputModel.getEmployeeId());
			leaveRequestId.setLeaveRequestIndex(nextLeaveRequestIndex);

			// Set the leave request ID and save the leave request
			leaveRequest.setLeaveRequestId(leaveRequestId);
			try {
				leaveRequestDAO.saveEmployeeLeaveRequest(leaveRequest);
			} catch (Exception e) {
				logger.info("Error saving the Leave request");
			}

			logger.info("Leave request submitted successfully");

			return ResponseEntity.ok("Success");

		} catch (Exception e) {
			logger.error("Failed to submit leave request.", e);
			String errorMessage = "Internal Server Error";
			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// to get leave requests at admin side
	@RequestMapping(value = "/leaveRequests", method = RequestMethod.GET)
	public String getAdminSideLeaveRequests(HttpSession session, Model model) {

		logger.info("Getting the leave requests");

		try {
			// Get the admin ID from the session
			int id = (int) session.getAttribute("adminId");

			// Retrieve employees for the HR and manager ID
			List<Employee> employees = employeeDAO.getEmployeesByHRAndManager(id);

			// Create a list to hold the output models
			List<EmployeeLeaveModel> outputmodel = new ArrayList<>();

			// Iterate over each employee
			for (Employee employee : employees) {
				// Retrieve leave requests for the employee
				List<EmployeeLeaveRequest> leaves = leaveRequestDAO
						.getEmployeeAndLeaveRequestData(employee.getEmplId());
				// Iterate over each leave request
				for (EmployeeLeaveRequest leave : leaves) {
					// Create a new leave model
					EmployeeLeaveModel leavemodel = context.getBean(EmployeeLeaveModel.class);
					// Set the properties of the leave model
					leavemodel.setEmpId(employee.getEmplId());
					leavemodel.setName(employee.getEmplFirstname() + employee.getEmplLastname());
					leavemodel.setLeaveRequestIndex(leave.getLeaveRequestId().getLeaveRequestIndex());
					leavemodel.setLeaveType(leave.getLeaveType());
					leavemodel.setLeaveStartDate(leave.getLeaveStartDate());
					leavemodel.setLeaveEndDate(leave.getLeaveEndDate());
					leavemodel.setReason(leave.getReason());
					// Add the leave model to the output list
					outputmodel.add(leavemodel);
				}

			}
			// Add the output model list to the model
			model.addAttribute("data", outputmodel);

			logger.info("The leave requests are successfully loaded");

			// Return the view name for rendering
			return "AdminLeaveRequests";

		} catch (Exception e) {
			// Handle any exceptions that may occur
			logger.error("Failed to retrieve leave requests.", e);
			return "error";
		}

	}

	// to reject leave
	@RequestMapping(value = "/rejectLeave", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<String> rejectLeave(@ModelAttribute EmployeeLeaveModel employeeLeaveModel) {
		try {

			logger.info("reject Leave requested");

			// set the employee ID and leave request index to leave request id from the model
			leaveRequestId.setEmployeeId(employeeLeaveModel.getEmpId());
			leaveRequestId.setLeaveRequestIndex(employeeLeaveModel.getLeaveRequestIndex());

			// Retrieve the employee leave request using the leave request ID
			EmployeeLeaveRequest employeeLeaveRequest = leaveRequestDAO.getEmployeeLeaveRequest(leaveRequestId);

			if (employeeLeaveRequest != null) {
				// Set the approvedBy field to -1 to indicate rejection
				employeeLeaveRequest.setApprovedBy(-1);
			}

			logger.info("Leave successfully rejected.");

		} catch (Exception e) {
			logger.error("Failed to reject leave request.", e);
			String errorMessage = "Internal Server Error";
			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return response
		return ResponseEntity.ok("successfully status updated");
	}

	// to accept leave
	@RequestMapping(value = "/acceptLeave", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<String> acceptLeave(@ModelAttribute EmployeeLeaveInputModel employeeLeaveInputModel,
			HttpSession session) {
		try {

			logger.info("/acceptLeave endpoint requested");

			// set the employee ID and leave request index to leave request id from the model
			leaveRequestId.setEmployeeId(employeeLeaveInputModel.getEmployeeId());
			leaveRequestId.setLeaveRequestIndex(employeeLeaveInputModel.getLeaveRequestIndex());

			// Retrieve the employee leave request using the leave request ID
			EmployeeLeaveRequest employeeLeaveRequest = leaveRequestDAO.getEmployeeLeaveRequest(leaveRequestId);

			int adminId = (int) session.getAttribute("adminId");

			if (employeeLeaveRequest != null) {
				// Set the approvedBy field to the admin ID
				employeeLeaveRequest.setApprovedBy(adminId);
				// Set the approved leave start date, end date, and remarks from the input model
				employeeLeaveRequest.setApprovedLeaveEndDate(
						LocalDate.parse(employeeLeaveInputModel.getLeaveEndDate(), DateTimeFormatter.ISO_DATE));
				employeeLeaveRequest.setApprovedLeaveStartDate(
						LocalDate.parse(employeeLeaveInputModel.getLeaveStartDate(), DateTimeFormatter.ISO_DATE));
				employeeLeaveRequest.setApprovedRemarks(employeeLeaveInputModel.getRemarks());
			}

			logger.info("Leave successfully accepted.");

		} catch (Exception e) {
			// Handle any exceptions that may occur
			logger.error("Failed to accept leave request.", e);
			String errorMessage = "Internal Server Error";
			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// Return a success response
		return ResponseEntity.ok("successfully status updated");
	}

	// to get admin approved leaves
	@RequestMapping(value = "/AdminapprovedLeaves", method = RequestMethod.GET)
	public String getAdminApprovedLeaves(Model model, HttpSession session) {

		try {

			logger.info("Getting admin approved Leaves");

			int id = (int) session.getAttribute("adminId");
			// Create a list to store the approved leave models
			List<ApprovedLeaveModel> approvedOutModel = new ArrayList<>();
			// Retrieve the approved employee and leave request data based on the admin ID
			List<EmployeeLeaveRequest> approvedLeaves = leaveRequestDAO.getApprovedEmployeeAndLeaveRequestData(id);
			for (EmployeeLeaveRequest leave : approvedLeaves) {
				// Retrieve the employee information for each approved leave
				Employee employee = employeeDAO.getEmployee(leave.getLeaveRequestId().getEmployeeId());
				// Create a new approved leave model
				ApprovedLeaveModel approvedLeaveModel = context.getBean(ApprovedLeaveModel.class);
				approvedLeaveModel.setEmployeeId(employee.getEmplId());
				approvedLeaveModel.setEmployeeName(employee.getEmplFirstname() + " " + employee.getEmplLastname());
				approvedLeaveModel.setApprovedStartDate(leave.getApprovedLeaveStartDate());
				approvedLeaveModel.setApprovedEndDate(leave.getApprovedLeaveEndDate());
				// Add the approved leave model to the list
				approvedOutModel.add(approvedLeaveModel);
			}
			// Add the approved leaves to the model attribute
			model.addAttribute("approvedLeaves", approvedOutModel);

			logger.info("Admin approved are successfully loaded.");

		} catch (Exception e) {
			logger.error("Failed to get admin approved leaves.", e);
			return "error";
		}
		// Return the view for displaying the admin-approved leaves
		return "AdminApprovedLeaves";
	}

	// to get employee leave history
	@RequestMapping(value = "/geEmployeeLeaves", method = RequestMethod.GET)
	public String getEmployeeLeavesHistory(Model model, HttpSession session) {

		try {

			logger.info("Getting Employee Leaves History");

			int id = (int) session.getAttribute("employeeId");
			// Create a list to store the employee leave models
			List<EmployeeLeaveModel> history = new ArrayList<>();
			// Retrieve the leave request history for the employee
			List<EmployeeLeaveRequest> employeeLeavesData = leaveRequestDAO.getLeaveRequestHistory(id);
			for (EmployeeLeaveRequest leave : employeeLeavesData) {
				// Create a new employee leave model
				EmployeeLeaveModel leavemodel = context.getBean(EmployeeLeaveModel.class);
				leavemodel.setLeaveRequestIndex(leave.getLeaveRequestId().getLeaveRequestIndex());
				leavemodel.setLeaveRequestDate(leave.getRequestDateTime());
				leavemodel.setLeaveStartDate(leave.getApprovedLeaveStartDate());
				leavemodel.setLeaveEndDate(leave.getApprovedLeaveEndDate());
				leavemodel.setLeaveType(leave.getLeaveType());
				leavemodel.setReason(leave.getReason());
				leavemodel.setStatus(leave.getApprovedBy());
				// Add the employee leave model to the list
				history.add(leavemodel);

			}
			// Add the leave history to the model attribute
			model.addAttribute("leavehistory", history);

			logger.info("successfully Loaded the Employees Leave History");

		} catch (Exception e) {
			logger.error("Failed to get Employee Leaves History.", e);
			return "error";
		}
		// Return the view for displaying the employee's leave history
		return "employeeLeaveHistory";
	}

	// get jobgradewise leaves
	@RequestMapping(value = "/getJobGradeWiseLeaves", method = RequestMethod.GET)
	public String getJobGradeWiseLeaves(Model model) {
		try {
			logger.info("Getting Job Grade Wise Leaves");
			// Retrieve the job grade-wise leaves from the DAO
			List<JobGradeWiseLeaves> jobGradeLeaves = leaveRequestDAO.getJobGradeWiseLeaves();
			// Create a list to store the job grade leaves output models
			List<JobGradeLeavesOutModel> result = new ArrayList<>();
			for (JobGradeWiseLeaves leaves : jobGradeLeaves) {
				// Create a new job grade leaves output model
				JobGradeLeavesOutModel leavedata = context.getBean(JobGradeLeavesOutModel.class);
				leavedata.setJobGradeId(leaves.getJbgrId());
				leavedata.setTotalLeaves(leaves.getTotalLeavesPerYear());
				leavedata.setSickLeaves(leaves.getSickLeavesPerYear());
				leavedata.setCasualLeaves(leaves.getCasualLeavesPerYear());
				leavedata.setOtherLeaves(leaves.getOtherLeavesPerYear());
				// Add the job grade leaves output model to the list
				result.add(leavedata);
			}

			// get all the existing job grades
			List<HrmsJobGrade> existingJobGrades = holidayDAO.getAllJobGradesInfo();

			// Add the job grade leaves to the model attribute
			model.addAttribute("jobgradeleaves", result);

			model.addAttribute("jobgradeinfo", existingJobGrades);

			logger.info("Job Grade Wise Leaves are loaded successfully.");

		} catch (Exception e) {
			logger.error("Failed to get Job Grade Wise Leaves.", e);
			return "error";
		}
		// Return the view for displaying the job grade-wise leaves
		return "jobGradeWiseLeaves";
	}

	// get leave statistics for dashboard
	@RequestMapping(value = "/getLeaveStatistics", method = RequestMethod.GET)
	public ResponseEntity<String> getLeaveStatistics(HttpSession session) {
		try {

			logger.info("Getting Leave Statistics");

			int id = (int) session.getAttribute("employeeId");
			// Retrieve the employee information from the DAO using the employee ID
			Employee employee = employeeDAO.getEmployee(id);

			// Retrieve the leaves provided statistics based on the job grade ID of the employee from the DAO
			JobGradeWiseLeaves leavesProvidedStatistics = leaveRequestDAO
					.getJobGradeWiseLeaves(employee.getEmplJbgrId().trim());

			// Retrieve the approved leave requests for the employee ID and the current year from the DAO
			List<EmployeeLeaveRequest> leaves = leaveRequestDAO.getApprovedLeaveRequests(employee.getEmplId(),
					Year.now().getValue());
			// Calculate the leaves taken based on the retrieved leave request data and leaves provided statistics using
			// the service method
			LeaveValidationModel validation = employeeService.calculateLeavesTaken(leaves, leavesProvidedStatistics);

			logger.info("Leave Statistics Loaded Successfully");

			return ResponseEntity.ok(gson.toJson(validation));
		} catch (Exception e) {
			logger.error("Failed to get Leave Statistics.", e);
			String errorMessage = "Internal Server Error";
			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/addjobgradeleaves", method = RequestMethod.POST)
	public ResponseEntity<String> addJobGradeLeaves(@ModelAttribute JobGradeLeavesOutModel jobGradeLeavesmodel) {
		jobGradeWiseLeaves.setJbgrId(jobGradeLeavesmodel.getJobGradeId());
		jobGradeWiseLeaves.setCasualLeavesPerYear(jobGradeLeavesmodel.getCasualLeaves());
		jobGradeWiseLeaves.setTotalLeavesPerYear(jobGradeLeavesmodel.getTotalLeaves());
		jobGradeWiseLeaves.setSickLeavesPerYear(jobGradeLeavesmodel.getSickLeaves());
		jobGradeWiseLeaves.setOtherLeavesPerYear(jobGradeLeavesmodel.getOtherLeaves());

		leaveRequestDAO.saveJobGradeLeaveRequest(jobGradeWiseLeaves);

		return ResponseEntity.ok("jobgrade wise leaves data is added successfully");

	}

	@RequestMapping(value = "/updatejobgradeleaves", method = RequestMethod.POST)
	public ResponseEntity<String> updateJobGradeLeaves(@ModelAttribute JobGradeLeavesOutModel jobGradeLeavesmodel) {
		leaveRequestDAO.updateJobGradeLeaveRequest(jobGradeLeavesmodel);

		return ResponseEntity.ok("jobgrade wise leaves data is added successfully");

	}

}