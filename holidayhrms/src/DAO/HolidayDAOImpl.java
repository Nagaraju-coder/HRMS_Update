package DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import DAO_Interfaces.HolidayDAO;
import models.GradeHoliday;
import models.Holiday;
import models.HrmsJobGrade;
import models.JobGradeModel;

@Repository
public class HolidayDAOImpl implements HolidayDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public List<Holiday> findAllHolidays() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Holiday> cq = cb.createQuery(Holiday.class);
		Root<Holiday> root = cq.from(Holiday.class);
		cq.select(root);
		cq.orderBy(cb.asc(root.get("hday_date")));
		return entityManager.createQuery(cq).getResultList();
	}

	@Override
	public GradeHoliday findHolidayById(String id) {
		return entityManager.find(GradeHoliday.class, id);
	}

	@Override
	@Transactional
	public List<GradeHoliday> findAllGradeHolidays() {
		TypedQuery<GradeHoliday> query = entityManager.createQuery("SELECT gh FROM GradeHoliday gh",
				GradeHoliday.class);
		return query.getResultList();
	}
	
	public List<HrmsJobGrade> getAllJobGradesInfo(){
		TypedQuery<HrmsJobGrade> query = entityManager.createQuery("SELECT jg FROM HrmsJobGrade jg",
				HrmsJobGrade.class);
		return query.getResultList();
	}
	
	public void saveJobGrade(HrmsJobGrade jobgrade) {
		entityManager.persist(jobgrade);
	}

}
