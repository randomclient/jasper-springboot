
package com.exercise.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exercise.dao.ClassService;
import com.exercise.dao.StudentService;
import com.exercise.dto.ClassDto;
import com.exercise.dto.StudentDto;
import com.exercise.model.StudentBean;
import com.exercise.service.ReportService;

import net.sf.jasperreports.engine.JRException;

@Controller
@RequestMapping("/stumgmt")
public class studentController {

	@Autowired
	private ReportService reportService;
	
	@Autowired
	private StudentService studentService;

	@Autowired
	private ClassService classService;

	
	@GetMapping("/studentpdf")
	public ResponseEntity<byte[]> generateReport() throws FileNotFoundException, JRException {
		return reportService.generateStudentPdf();
	}
	
	@GetMapping(value = "/addstudent")
	public ModelAndView addstudent(ModelMap map) {
		ClassDto dto = new ClassDto();
		dto.setId("");
		dto.setName("");
		List<ClassDto> list = classService.getAllClass();
		map.addAttribute("clist", list);
		return new ModelAndView("BUD002", "student", new StudentBean());
	}

	@PostMapping(value = "/setupaddstudent")
	public String setupaddstudent(@ModelAttribute("student") @Validated StudentBean bean, BindingResult bs,
			ModelMap map) {
		if (bs.hasErrors()) {
			return "BUD002";
		}
		StudentDto dto = new StudentDto();
		dto.setStudentId(bean.getStudentId());
		dto.setStudentName(bean.getStudentName());
		dto.setClassName(bean.getClassName());
		dto.setRegister(bean.getRegister());
		dto.setStatus(bean.getStatus());
		System.out.println(dto.getStudentId());
		Optional<StudentDto> record = studentService.getStudentById(dto.getStudentId());
		if (!record.isEmpty())
			map.addAttribute("err", "StudentId has been already");
		else {
			studentService.save(dto);
			map.addAttribute("msg", "Insert successfully!!");
		}
		return "BUD002";
	}

	@GetMapping(value = "/searchstudent")
	public ModelAndView searchstudent() {
		return new ModelAndView("BUD001", "student", new StudentBean());
	}

	@PostMapping("/setupsearchstudent")
	public String setupsearchstudent(@ModelAttribute("student") StudentBean bean, ModelMap map) {
		StudentDto dto = new StudentDto();
		dto.setStudentId(bean.getStudentId());
		dto.setStudentName(bean.getStudentName());
		dto.setClassName(bean.getClassName());
		List<StudentDto> list = studentService.getAllStudent();
		System.out.println(dto.getStudentId());

		if (bean.getStudentId().equals("") && bean.getStudentName().equals("") && bean.getClassName().equals("")) {
			map.addAttribute("stulist", list);
		} else if (!bean.getStudentId().equals("") || !bean.getStudentName().equals("")
				|| !bean.getClassName().equals("")) {
			Optional<StudentDto> singleRow = studentService.getStudentById(dto.getStudentId());
			if (!singleRow.isEmpty()) {
				List<StudentDto> listOne = new ArrayList<>();
				listOne.add(singleRow.get());
				map.addAttribute("stulist", listOne);
			} else
				map.addAttribute("err", "Student not found!!");

		}
		return "BUD001";

	}

	@GetMapping(value = "/studentupdate")
	public ModelAndView updatestudent(@RequestParam("id") String studentId, Model model) {
		ClassDto cdto = new ClassDto();
		cdto.setId("");
		cdto.setName("");
		List<ClassDto> list = classService.getAllClass();
		model.addAttribute("clist", list);
		StudentDto dto = new StudentDto();
		dto.setStudentId(studentId);
		model.addAttribute("id", dto.getStudentId());
		return new ModelAndView("BUD002-01", "student", studentService.getStudentById(dto.getStudentId()));
	}

	@PostMapping(value = "/setupupdatestudent")
	public String setupupdatestudent(@ModelAttribute("student") @Validated StudentBean bean, BindingResult bs,
			ModelMap map) {

		StudentDto dto = new StudentDto();
		dto.setStudentId(bean.getStudentId());
		dto.setStudentName(bean.getStudentName());
		dto.setClassName(bean.getClassName());
		dto.setRegister(bean.getRegister());
		dto.setStatus(bean.getStatus());
		studentService.getAllStudent();

		studentService.update(dto, dto.getStudentId());

		map.addAttribute("msg", "Update successfully!!");

		return "BUD002-01";
	}

	@GetMapping(value = "/deletestudent")
	public String deletestudent(@RequestParam("id") String studentId, RedirectAttributes map) {
		StudentDto dto = new StudentDto();
		dto.setStudentId(studentId);
		studentService.delete(dto.getStudentId());

		map.addAttribute("msg", "Delete successful!!");

		return "redirect:/stumgmt/searchstudent";
	}

	@GetMapping(value = "/studentreset")
	public String reset() {
		return "redirect:/stumgmt/searchstudent";
	}

}
