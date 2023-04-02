package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.UserDtls;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	@GetMapping("/")
	public String index() {
		return "index";
	}
	@GetMapping("/signin")
	public String login() {
		return "login";
	}
	@GetMapping("/registration")
	public String registration() {
		return "registration";
	}
	
	@PostMapping("/createUser")
	public String createuser(@ModelAttribute UserDtls user, HttpSession session) {
		//System.out.println(user);
		boolean f =userService.checkEmail(user.getEmail());
		if(f) {
			session.setAttribute("msg", "email is already registered");
		}
		else {
			UserDtls userDtls =userService.createUser(user);
			
			if(userDtls!=null) {
				session.setAttribute("msg", "Registered successfully");
			}
			else {
				session.setAttribute("msg", "Something went wrong!!");
			}
			
		}
		
		return "redirect:/registration";
	}

}
