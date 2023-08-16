package com.example.demo.controller;

import java.net.URI;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.DatabaseFile;
import com.example.demo.model.PersonalDtls;
import com.example.demo.model.UserDtls;
import com.example.demo.repository.DatabaseFileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.personalRepository;
import com.example.demo.service.FileUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

	String reset = "\u001B[0m";
	String red = "\u001B[31m";
	String green = "\u001B[32m";
	String yellow = "\u001B[33m";
	String blue = "\u001B[34m";
	String cyan = "\u001B[36m";

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private personalRepository personalRepository;

	@Autowired
	private FileUtils fileUtils;

	@Autowired
	private DatabaseFileRepository fileRepo;

	@Autowired
	private jakarta.persistence.EntityManager em;

	@ModelAttribute
	private void userDetails(Model model, Principal p) {
		if (p != null) {
			String email = p.getName();
			UserDtls user = userRepo.findByEmail(email);
			model.addAttribute("user", user);
			PersonalDtls personalDtls = personalRepository.findByUser(user);
			model.addAttribute("personalDetails", personalDtls);
			System.out.println(red + "\n\nFrom personalDetails User:\n" + user + reset);
			System.out.println(red + "\n\nFrom personalDetails PersonalDtls:\n" + personalDtls + reset);
			System.out.println(red + "\n\nFrom personalDetails Model:\n" + model + reset);
			fileUtils.populateFileListsAndAddToModel(user, model);
			System.out.println(red + "\n\nFrom personalDetails Model after files:\n" + model + reset);

		} else {
			model.addAttribute("user", null);
		}
	}

	@GetMapping("/personalInfo")
	public String personalInfo(Model model, Principal p) {
		// String email = p.getName();
		// UserDtls user = userRepo.findByEmail(email);
		// PersonalDtls personalDtls = personalRepository.findByUser(user);
		// model.addAttribute("personalDetails", personalDtls);
		System.out.println(cyan + "\n\nModel from pinf" + model + reset);
		return "user/teacherFiles/personalInfo";
	}

	@GetMapping("/detailUpdateForm")
	public String updateDetails(Model model) {
		System.out.println(yellow + "\n\nFrom update details" + model + reset);
		return "user/teacherFiles/detailUpdateForm";
	}

	@PostMapping("/update-user-details")
	public String updateUser(@ModelAttribute("personalDetails") PersonalDtls updatedPersonalDtls) {
		System.out.println(yellow + "\n\nFrom update Teacher existingpd" + updatedPersonalDtls + reset);
		try {
			personalRepository.save(updatedPersonalDtls);
			return "redirect:/teacher/personalInfo";

		} catch (Exception e) {
			System.err.println("Error in updating user details");
			e.printStackTrace();
		}
		return "redirect:/teacher/update-user-details";
	}

	@GetMapping("/deleteFile/{id}")
	public ResponseEntity<?> deleteFile(@PathVariable String id, HttpServletRequest request, Model model) {
		Optional<DatabaseFile> optionalFile = fileRepo.findById(id);
		if (!optionalFile.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		DatabaseFile file = optionalFile.get();
		if (!file.getUser().equals(model.getAttribute("user"))) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		em.detach(file.getUser());
		try {
			fileRepo.delete(file);
			fileRepo.flush();
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}

		// Redirect back to the referring page after the delete
		String referer = request.getHeader("Referer");
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(referer)).build();
	}

	@GetMapping("/")
	public String home() {
		return "user/teacherFiles/teacher";
	}

	@GetMapping("/teacher-dashboard")
	public String dashboard() {
		return "user/teacherFiles/teacherDashboard";
	}

	@GetMapping("/research")
	public String research() {
		return "user/teacherFiles/researchPublications";
	}

	@GetMapping("/awards")
	public String awards() {
		return "user/teacherFiles/awardsAchievements";
	}

	@GetMapping("/interactions")
	public String interactions() {
		return "user/teacherFiles/interactions";
	}

	@GetMapping("/fdp")
	public String fdp() {
		return "user/teacherFiles/fdp";
	}

	@GetMapping("/conferenceWorkshopSeminar")
	public String conferenceWorkshopSeminar() {
		return "user/teacherFiles/conferenceWorkshopSeminar";
	}

	@GetMapping("/guestLectAndIndustrialVisits")
	public String guestLectAndIndustrialVisits() {
		return "user/teacherFiles/guestLectAndIndustrialVisits";
	}

	@GetMapping("/settings")
	public String settings() {
		return "user/teacherFiles/settings";
	}

	@GetMapping("/changePass")
	public String loadChangePassword() {
		return "user/teacherFiles/settings";
	}

	@PostMapping("/updatePassword")
	public String changePassword(Principal p, @RequestParam String oldPass,
			@RequestParam String newPass, HttpSession session) {

		String email = p.getName();
		UserDtls loginUser = userRepo.findByEmail(email);

		boolean f = passwordEncoder.matches(oldPass, loginUser.getPassword());

		if (f) {
			loginUser.setPassword(passwordEncoder.encode(newPass));
			UserDtls updatePasswordUser = userRepo.save(loginUser);
			if (updatePasswordUser != null) {
				System.out.println("password changed successfully");
			} else {
				System.out.println("something went wrong");
			}

		} else {
			System.out.println("incorrect password");

		}

		return "redirect:/teacher/settings";

	}

	@PostMapping("/deleteUser")
	public String deleteUser(Principal principal, @RequestParam String password, RedirectAttributes redirectAttributes,
			HttpSession session) {
		String email = principal.getName();
		UserDtls user = userRepo.findByEmail(email);

		// Check if user is admin
		if ("ROLE_ADMIN".equals(user.getRole())) {
			return "redirect:/logout";
		}

		if (passwordEncoder.matches(password, user.getPassword())) {
			// Delete the user
			userRepo.delete(user);

			// // Invalidate the session
			// session.invalidate();

			redirectAttributes.addFlashAttribute("message", "User deleted successfully");
			return "redirect:/logout";
		} else {
			redirectAttributes.addFlashAttribute("error", "Incorrect password");
			return "redirect:/teacher/settings";
		}
	}

}