package com.exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.exercise.dao.UserService;
import com.exercise.service.ReportService;

@Controller
public class WelcomeController {

	@Autowired
	UserService userService;

	@Autowired
	ReportService reportService;

	@GetMapping("/")
	public String showMyLoginPage() {
		return "LGN001";
	}

}
