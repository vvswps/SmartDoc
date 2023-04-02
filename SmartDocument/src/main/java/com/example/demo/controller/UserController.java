package com.example.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.UserDtls;
import com.example.demo.repositary.UserRepositary;

import ch.qos.logback.core.model.Model;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepositary userRepo;

	@ModelAttribute
	private void userDetails(Model model, Principal p) {
		String email = p.getName();
		UserDtls user = userRepo.findByEmail(email);

	    model.addAttribute("user", user);

	}
	@GetMapping("/")
	public String home() {
		return "user/home";
	}

}
