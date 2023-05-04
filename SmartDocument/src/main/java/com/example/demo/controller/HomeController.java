package com.example.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.UserDtls;
import com.example.demo.repositary.UserRepositary;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepositary userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncode;

	@ModelAttribute
	private void userDetails(Model model, Principal p) {
		if (p != null) {
			String email = p.getName();
			UserDtls user = userRepo.findByEmail(email);

			model.addAttribute("user", user);
		}

	}

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/signin")
	public String login() {
		// System.out.println("Login page open");
		return "login";
	}

	@GetMapping("/registration")
	public String registration() {
		return "registration";
	}

	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}

	// @PostMapping("/processLogin")
	// public String login(@ModelAttribute UserDtls user) {
	// System.out.println("Login clicked");
	// return "redirect:/dashboard";
	// }
	@PostMapping("/createUser")
	public String createuser(@ModelAttribute UserDtls user, HttpSession session, @RequestParam("role") String role) {
		// System.out.println(user);
		boolean f = userService.checkEmail(user.getEmail());
		if (f) {
			session.setAttribute("msg", "email is already registered");
		} else {
			UserDtls userDtls = userService.createUser(user, role);

			if (userDtls != null) {
				session.setAttribute("msg", "Registered successfully, Redirecting to login in 3sec");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "redirect:/signin";
			} else {
				session.setAttribute("msg", "Something went wrong!!");
			}

		}

		return "redirect:/registration";
	}

	@GetMapping("/loadForgotPassword")
	public String loadForgotPassword() {
		return "forgot_password";
	}

	@GetMapping("/loadResetPassword/{id}")
	public String loadResetPassword(@PathVariable int id, Model m) {
		// System.out.println(id);
		m.addAttribute("id", id);
		return "reset_password";
	}

	@PostMapping("/forgotPassword")
	public String forgotPassword(@RequestParam String email, @RequestParam String mobileNum) {
		UserDtls user = userRepo.findByEmailAndMobileNumber(email, mobileNum);
		if (user != null) {
			return "redirect:/loadResetPassword/" + user.getId();
		} else {
			System.out.println("invalid ");
			return "forgot_password";
		}

	}

	@PostMapping("/changePassword")
	public String resetPassword(@RequestParam String psw, @RequestParam Integer id) {

		UserDtls user = userRepo.findById(id).get();
		String encryptPsw = passwordEncode.encode(psw);
		user.setPassword(encryptPsw);
		UserDtls updateUser = userRepo.save(user);
		if (updateUser != null) {
			System.out.println("updated");
		} else {
			System.out.println("not updated");
		}
		return "redirect:/signin";
	}

}
