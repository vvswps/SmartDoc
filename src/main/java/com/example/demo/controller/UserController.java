/*
 * This is the UserController class which handles user-related operations.
 * It is responsible for handling requests related to user profile, password change, and updating the password.
 */

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
import com.example.demo.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/*
	 * This method is responsible for adding the logged-in user's details to the
	 * model attribute.
	 * The user's details are obtained from the user repository using the email
	 * obtained from Principal.
	 * The user object is added to the model to be used in the views.
	 */
	@ModelAttribute
	private void userDetails(Model model, Principal principal) {
		String email = principal.getName();
		UserDtls user = userRepo.findByEmail(email);
		model.addAttribute("user", user);
	}

	/*
	 * This method handles the GET request for loading the change password page.
	 * It returns the "user/change_password" view to display the change password
	 * page.
	 */
	@GetMapping("/changePass")
	public String loadChangePassword() {
		return "user/change_password";
	}

	/*
	 * This method handles the POST request for updating the user's password.
	 * It takes the old password, new password, Principal, and HttpSession as
	 * parameters.
	 * It checks if the old password provided matches the user's current password.
	 * If the passwords match, it encrypts and updates the new password in the
	 * repository.
	 * Finally, it redirects the user to the sign-in page.
	 */
	@PostMapping("/updatePassword")
	public String changePassword(Principal principal, @RequestParam String oldPass,
			@RequestParam String newPass, HttpSession session) {

		String email = principal.getName();
		UserDtls loginUser = userRepo.findByEmail(email);

		boolean isPasswordMatched = passwordEncoder.matches(oldPass, loginUser.getPassword());

		if (isPasswordMatched) {
			loginUser.setPassword(passwordEncoder.encode(newPass));
			UserDtls updatedUser = userRepo.save(loginUser);
			if (updatedUser != null) {

			} else {

			}
		} else {

		}
		return "redirect:/signin";
	}
}
