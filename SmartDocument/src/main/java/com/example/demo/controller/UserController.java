package com.example.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.UserDtls;
import com.example.demo.repositary.UserRepositary;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepositary userRepo;
	@Autowired
	private BCryptPasswordEncoder passwordEncode;

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
	@GetMapping("/changePass")
	public String loadChangePassword() {
		return "user/change_password";
	}
	@PostMapping("/updatePassword")
	public String changePassword(Principal p,@RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass,HttpSession session) {
		
		String email=p.getName();
		UserDtls loginUser=userRepo.findByEmail(email);
		
		boolean f=passwordEncode.matches(oldPass,loginUser.getPassword());
		
		if(f) {
			loginUser.setPassword(passwordEncode.encode(newPass));
			UserDtls updatePasswordUser=userRepo.save(loginUser);
			if(updatePasswordUser!=null) {
				System.out.println("password changed successfully");
			}else {
				System.out.println("something went wrong");
			}
			
			
		}else {
			System.out.println("incorrect password");
			
		}
		
		return "redirect:/signin";
		
	}

}
