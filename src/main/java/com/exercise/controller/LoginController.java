package com.exercise.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exercise.dao.UserService;
import com.exercise.dto.UserDto;
import com.exercise.model.UserBean;
import com.exercise.service.ReportService;

import net.sf.jasperreports.engine.JRException;

@Controller
@RequestMapping("/stumgmt")
public class LoginController {
	@Autowired
	private UserService userService;

	@Autowired
	private ReportService reportService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	/* go to main page */
	@GetMapping("/page")
	public String page() {
		return "M00001";
	}
	
	/* go to report page */
	@GetMapping("/userpdf")
	public ResponseEntity<byte[]> generateReport() throws FileNotFoundException, JRException {
		return reportService.generatePdf();
	}

	/* logout */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	/* searching */
	@GetMapping(value = "/usersearch")

	public ModelAndView usersearch() {
		return new ModelAndView("USR001", "user", new UserBean());
	}

	/* registering */
	@GetMapping(value = "/useradd")
	public ModelAndView useradd() {
		return new ModelAndView("USR002", "user", new UserBean());
	}

	@PostMapping("/setupadduser")
	public String setupuseradd(@ModelAttribute("user") @Validated UserBean bean, BindingResult bs, ModelMap map) {
		if (bs.hasErrors()) {

			return "USR002";
		}

		// validate password
		if (bean.getPassword().equals(bean.getConfirm())) {
			String encodePassword = passwordEncoder.encode(bean.getPassword());
			UserDto dto = new UserDto(); // define dto for getting data from front-end bean
			dto.setId(bean.getId());
			dto.setUser(bean.getUser());
			dto.setPassword(encodePassword);
			dto.setEnable(bean.isEnable());
			dto.setRole(bean.getRole());

			Optional<UserDto> optional = userService.getUserById(dto.getId());

			if (!optional.isEmpty())
				map.addAttribute("err", "User Id has been already!!");
			else {
				userService.save(dto);

				map.addAttribute("msg", "Insert successfully");
				return "USR002";
			}

		} else
			map.addAttribute("err", "Password are not match");

		return "USR002";
	}

	/* registering-search */
	@PostMapping("/setupusersearch")
	public String setupusersearch(@ModelAttribute("user") UserBean bean, ModelMap map) {
		UserDto dto = new UserDto();
		dto.setId(bean.getId());
		dto.setUser(bean.getUser());
		List<UserDto> list = userService.getAllUsers();

		if (bean.getId()==null && bean.getUser().equals("")) {
			map.addAttribute("userlist", list);
		} else if (!Integer.toString(bean.getId()).equals("") || !bean.getUser().equals("")) {

			if (!userService.getUserById(dto.getId()).isEmpty()) {
				List<UserDto> list1 = new ArrayList<>();
				list1.add(userService.getUserById(dto.getId()).get());
				map.addAttribute("userlist", list1);
			} else {
				map.addAttribute("msg", "User not found");
			}
		}
		return "USR001";
	}


	/* updating */
	@GetMapping(value = "/userupdate")

	public ModelAndView userupdate(@RequestParam("id") Integer id) {
		UserDto dto = new UserDto();
		dto.setId(id);
		return new ModelAndView("USR002-01", "user", userService.getUserById(dto.getId()));
	}

	@PostMapping("/userupdate")
	public String updateuser(@ModelAttribute("user") @Validated UserBean bean, BindingResult bs, ModelMap map) {
		if (bs.hasErrors()) {

			return "USR002-01";
		}
		if (bean.getPassword().equals(bean.getConfirm())) { 
			String encodePassword = passwordEncoder.encode(bean.getPassword());
			UserDto dto = new UserDto();
			dto.setId(bean.getId());
			dto.setUser(bean.getUser());
			dto.setPassword(encodePassword);
			dto.setEnable(bean.isEnable());
			dto.setRole(bean.getRole());

//			userService.getAllUsers();

			userService.update(dto, dto.getId());
			map.addAttribute("msg", "Update successfully");
			return "USR002-01";
		}
		map.addAttribute("err", "Password are not match");

		return "USR002-01";
	}

	/* deleting */
	@GetMapping(value = "/userdelete")
	public String userdelete(@RequestParam("id") String id, RedirectAttributes map) {
		UserDto dto = new UserDto();
		dto.setId(Integer.valueOf(id));
		userService.delete(dto);
		map.addAttribute("msg", "Delete successful!!");

		return "redirect:/stumgmt/usersearch";
	}
	
	/* resetting */
	@GetMapping(value = "userreset")
	public String reset() {
		return "redirect:/stumgmt/usersearch";
	}
}
