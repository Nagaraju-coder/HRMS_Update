package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import DAO_Interfaces.HolidayDAO;
import models.GradeHoliday;
import models.Holiday;
import models.HrmsJobGrade;
import models.JobGradeModel;

@Controller
public class HolidayController {

	private final HolidayDAO hd;
    private HrmsJobGrade jobGrade;
	
   
@Autowired
	public HolidayController(HolidayDAO hd, HrmsJobGrade jobGrade) {
		super();
		this.hd = hd;
		this.jobGrade = jobGrade;
	}

	// to get list of holidays
	@RequestMapping("/holidaysupd")
	public String showHolidays(Model model) {
		List<Holiday> holidays = hd.findAllHolidays();
		model.addAttribute("holidays", holidays);
		return "holidays";
	}

	// to get list of grade wise holidays
	@RequestMapping("/getgradewiseholidays")
	public String getgradewiseHolidays(Model model) {
		List<GradeHoliday> gradeholidays = hd.findAllGradeHolidays();
		model.addAttribute("gradeholidays", gradeholidays);
		return "gradeholidays";
	}
	
	@RequestMapping(value="/getJobGradeList",method=RequestMethod.GET)
	public String getJobGradesList(Model model) {
		List<HrmsJobGrade> info = hd.getAllJobGradesInfo();
		model.addAttribute("gradeInfo", info);
		return "JobGrades";
	}
	
	
	@RequestMapping(value="/addGrades",method=RequestMethod.POST)
	@Transactional
	public ResponseEntity<String> addGrades(@ModelAttribute JobGradeModel jobgrade) {
		try {
	    jobGrade.setId(jobgrade.getJbgrId());
	    jobGrade.setName(jobgrade.getJbgrName());
	    jobGrade.setDescription(jobgrade.getJbgrDescription());
	    hd.saveJobGrade(jobGrade);
	    
	    return ResponseEntity.ok("success");
		}catch(Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
		}
	}
}
