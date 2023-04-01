package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.UserDtls;
import com.example.demo.service.UserService;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	@GetMapping("/")
	public String index() {
		return "index";
	}
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@GetMapping("/registration")
	public String registration() {
		return "registration";
	}
	
	@PostMapping("/createUser")
	public String createuser(@ModelAttribute UserDtls user) {
		//System.out.println(user);
		
		UserDtls userDtls =userService.createUser(user);
		
		if(userDtls!=null) {
			System.out.println("successfulll");
		}
		else {
			System.out.println("something went wrong");
		}
		return "redirect:/registration";
	}

}
