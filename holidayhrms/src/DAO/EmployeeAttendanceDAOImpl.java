package DAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import DAO_Interfaces.EmployeeAttendanceDAO;
import models.EmployeeAttendance;

@Repository
public class EmployeeAttendanceDAOImpl implements EmployeeAttendanceDAO {

	private static Logger logger = LoggerFactory.getLogger(EmployeeAttendanceDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	// Saves the employee attendance record to the database.
	@Override
	public void save(EmployeeAttendance employeeAttendance) {
		entityManager.persist(employeeAttendance);
	}

	// Retrieves the next attendance request index for the given employee ID.
	@Override
	public int getNextAttendanceRequestIndex(int employeeId) {
		String queryString = "SELECT COALESCE(MAX(ea.attendanceId.emplPIndex), 0) + 1 " + "FROM EmployeeAttendance ea "
				+ "WHERE ea.attendanceId.employeeId = :employeeId";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("employeeId", employeeId);

		int nextIndex = (Integer) query.getSingleResult();

		logger.info("Next attendance request index for employeeId {}: {}", employeeId, nextIndex);

		return nextIndex;

	}

	// Retrieves the punch-in and punch-out data for yesterday for the given employee ID.
	@Override
	public List<Object[]> getYesterdayPunchInAndPunchOut(int employeeId) {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		LocalDateTime startOfDay = LocalDateTime.of(yesterday, LocalTime.MIN);
		LocalDateTime endOfDay = LocalDateTime.of(yesterday, LocalTime.MAX);

		// query to get punch data from the 00:00 to 23:59:59 of yesterday
		String queryString = "SELECT ea.punchIn, ea.punchOut FROM EmployeeAttendance ea "
				+ "WHERE ea.attendanceId.employeeId = :employeeId " + "AND ea.punchIn >= :startOfDay "
				+ "AND ea.punchOut <= :endOfDay order by ea.punchIn";

		TypedQuery<Object[]> query = entityManager.createQuery(queryString, Object[].class);
		query.setParameter("employeeId", employeeId);
		query.setParameter("startOfDay", startOfDay);
		query.setParameter("endOfDay", endOfDay);

		List<Object[]> results = query.getResultList();

		logger.info("Retrieved yesterday's punch-in and punch-out data for employeeId {}", employeeId);

		return results;

	}

	// Retrieves the punch-in and punch-out data for the specified year, month, and employee ID.
	@Override
	public List<Object[]> getPunchInAndPunchOutDataForYearAndMonthAndEmployee(int employeeId, int selectedYear,
			int selectedMonth) {
		String queryString = "SELECT ea.punchIn, ea.punchOut FROM EmployeeAttendance ea "
				+ "WHERE ea.attendanceId.employeeId = :employeeId " + "AND YEAR(ea.punchIn) = :selectedYear "
				+ "AND MONTH(ea.punchIn) = :selectedMonth order by ea.punchIn";

		Query query = entityManager.createQuery(queryString);
		query.setParameter("employeeId", employeeId);
		query.setParameter("selectedYear", selectedYear);
		query.setParameter("selectedMonth", selectedMonth);

		List<Object[]> results = query.getResultList();

		logger.info("Retrieved punch-in and punch-out data for employeeId {} in year {} and month {}: {}", employeeId,
				selectedYear, selectedMonth);

		return results;
	}

}
