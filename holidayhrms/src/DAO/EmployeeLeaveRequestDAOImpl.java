package DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import DAO_Interfaces.EmployeeLeaveRequestDAO;
import models.EmployeeLeaveRequest;
import models.EmployeeLeaveRequestId;
import models.JobGradeWiseLeaves;

@Repository
public class EmployeeLeaveRequestDAOImpl implements EmployeeLeaveRequestDAO {

	private static Logger logger = LoggerFactory.getLogger(EmployeeLeaveRequestDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	// Saves the employee leave request to the database.
	@Override
	public void saveEmployeeLeaveRequest(EmployeeLeaveRequest leaveRequest) throws Exception {
		entityManager.persist(leaveRequest);
		logger.info("Employee leave request saved successfully.");
	}

	// Retrieves the next leave request index for the given employee ID.
	@Override
	public int getNextLeaveRequestIndex(int employeeId) {
		String queryString = "SELECT COALESCE(MAX(lr.leaveRequestId.leaveRequestIndex), 0) + 1 "
				+ "FROM EmployeeLeaveRequest lr " + "WHERE lr.leaveRequestId.employeeId = :employeeId";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("employeeId", employeeId);
		return (Integer) query.getSingleResult();
	}

	// Retrieves the employee and leave request data for the given employee ID.
	@Override
	public List<EmployeeLeaveRequest> getEmployeeAndLeaveRequestData(int id) {
		String jpqlQuery = "SELECT elrq FROM EmployeeLeaveRequest elrq "
				+ "WHERE elrq.leaveRequestId.employeeId = :employeeIds " + "AND elrq.approvedBy = 0";
		TypedQuery<EmployeeLeaveRequest> query = entityManager.createQuery(jpqlQuery, EmployeeLeaveRequest.class);
		query.setParameter("employeeIds", id);
		List<EmployeeLeaveRequest> result = query.getResultList();
		return result;
	}

	// Retrieves the approved and pending employee leave requests for the given employee ID and year.
	@Override
	public List<EmployeeLeaveRequest> getApprovedAndPendingEmployeeAndLeaveRequestData(int id, int year) {
		String jpqlQuery = "SELECT elrq FROM EmployeeLeaveRequest elrq"
				+ " WHERE elrq.leaveRequestId.employeeId = :employeeIds" + " AND elrq.approvedBy != -1"
				+ " AND EXTRACT(YEAR FROM elrq.requestDateTime) = :year";
		TypedQuery<EmployeeLeaveRequest> query = entityManager.createQuery(jpqlQuery, EmployeeLeaveRequest.class);
		query.setParameter("employeeIds", id);
		query.setParameter("year", year);
		List<EmployeeLeaveRequest> result = query.getResultList();
		return result;
	}

	// Retrieves the approved employee leave requests for the given employee ID.
	@Override
	public List<EmployeeLeaveRequest> getApprovedEmployeeAndLeaveRequestData(int id) {
		String jpqlQuery = "SELECT elrq FROM EmployeeLeaveRequest elrq " + "WHERE "
				+ " elrq.approvedBy = :employeeIds ";
		TypedQuery<EmployeeLeaveRequest> query = entityManager.createQuery(jpqlQuery, EmployeeLeaveRequest.class);
		query.setParameter("employeeIds", id);
		List<EmployeeLeaveRequest> result = query.getResultList();
		return result;
	}

	// Retrieves the employee leave request for the given leave request ID.
	@Override
	public EmployeeLeaveRequest getEmployeeLeaveRequest(EmployeeLeaveRequestId key) {
		return entityManager.find(EmployeeLeaveRequest.class, key);
	}

	// Retrieves the job grade wise leaves provided statistics for the given job grade ID.
	@Override
	public JobGradeWiseLeaves getJobGradeWiseLeaves(String jobGradeId) {
		return entityManager.find(JobGradeWiseLeaves.class, jobGradeId);
	}

	// Retrieves the leave request history for the given employee ID.
	@Override
	public List<EmployeeLeaveRequest> getLeaveRequestHistory(int id) {
		String jpqlQuery = "SELECT elrq FROM EmployeeLeaveRequest elrq " + "WHERE "
				+ " elrq.leaveRequestId.employeeId = :employeeIds ";
		TypedQuery<EmployeeLeaveRequest> query = entityManager.createQuery(jpqlQuery, EmployeeLeaveRequest.class);
		query.setParameter("employeeIds", id);
		List<EmployeeLeaveRequest> result = query.getResultList();
		return result;
	}

	// Retrieves the job grade wise leaves provided statistics for all job grades.
	@Override
	public List<JobGradeWiseLeaves> getJobGradeWiseLeaves() {
		String jpqlQuery = "SELECT jgwl FROM JobGradeWiseLeaves jgwl ";
		TypedQuery<JobGradeWiseLeaves> query = entityManager.createQuery(jpqlQuery, JobGradeWiseLeaves.class);
		List<JobGradeWiseLeaves> result = query.getResultList();
		return result;
	}

	// Retrieves the approved leave requests for the given employee ID and year.
	@Override
	public List<EmployeeLeaveRequest> getApprovedLeaveRequests(int id, int year) {

		String jpqlQuery = "SELECT elrq FROM EmployeeLeaveRequest elrq "
				+ "WHERE elrq.leaveRequestId.employeeId = :employeeId" + " AND elrq.approvedBy > 0"
				+ " AND EXTRACT(YEAR FROM elrq.requestDateTime) = :year";

		TypedQuery<EmployeeLeaveRequest> query = entityManager.createQuery(jpqlQuery, EmployeeLeaveRequest.class);
		query.setParameter("employeeId", id);
		query.setParameter("year", year);
		List<EmployeeLeaveRequest> result = query.getResultList();
		return result;
	}

}