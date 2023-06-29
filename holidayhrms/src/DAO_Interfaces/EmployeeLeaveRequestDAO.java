package DAO_Interfaces;

import java.util.List;

import models.EmployeeLeaveRequest;
import models.EmployeeLeaveRequestId;
import models.JobGradeWiseLeaves;

public interface EmployeeLeaveRequestDAO {

	/**
	 * Saves the employee leave request to the database.
	 * 
	 * @param leaveRequest The employee leave request to be saved.
	 */
	void saveEmployeeLeaveRequest(EmployeeLeaveRequest leaveRequest) throws Exception;

	/**
	 * Retrieves the next leave request index for the given employee ID.
	 * 
	 * @param employeeId The ID of the employee.
	 * @return The next leave request index.
	 */
	int getNextLeaveRequestIndex(int employeeId);

	/**
	 * Retrieves the employee and leave request data for the given employee ID.
	 * 
	 * @param id The ID of the employee.
	 * @return A list of employee leave requests.
	 */
	List<EmployeeLeaveRequest> getEmployeeAndLeaveRequestData(int id);

	/**
	 * Retrieves the employee leave request for the given leave request ID.
	 * 
	 * @param id The leave request ID.
	 * @return The employee leave request.
	 */
	EmployeeLeaveRequest getEmployeeLeaveRequest(EmployeeLeaveRequestId id);

	/**
	 * Retrieves the job grade wise leaves provided statistics for the given job grade ID.
	 * 
	 * @param jobGradeId The job grade ID.
	 * @return The job grade wise leaves provided statistics.
	 */
	JobGradeWiseLeaves getJobGradeWiseLeaves(String jobGradeId);

	/**
	 * Retrieves the approved and pending employee leave requests for the given employee ID and year.
	 * 
	 * @param id   The ID of the employee.
	 * @param year The year.
	 * @return A list of approved and pending employee leave requests.
	 */
	List<EmployeeLeaveRequest> getApprovedAndPendingEmployeeAndLeaveRequestData(int id, int year);

	/**
	 * Retrieves the approved employee leave requests for the given employee ID.
	 * 
	 * @param id The ID of the employee.
	 * @return A list of approved employee leave requests.
	 */
	List<EmployeeLeaveRequest> getApprovedEmployeeAndLeaveRequestData(int id);

	/**
	 * Retrieves the leave request history for the given employee ID.
	 * 
	 * @param id The ID of the employee.
	 * @return A list of leave request history.
	 */
	List<EmployeeLeaveRequest> getLeaveRequestHistory(int id);

	/**
	 * Retrieves the job grade wise leaves provided statistics for all job grades.
	 * 
	 * @return A list of job grade wise leaves provided statistics.
	 */
	List<JobGradeWiseLeaves> getJobGradeWiseLeaves();

	/**
	 * Retrieves the approved leave requests for the given employee ID and year.
	 * 
	 * @param id   The ID of the employee.
	 * @param year The year.
	 * @return A list of approved leave requests.
	 */
	List<EmployeeLeaveRequest> getApprovedLeaveRequests(int id, int year);
}
