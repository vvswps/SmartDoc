package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.DatabaseFile;
import com.example.demo.model.PersonalDtls;
import com.example.demo.model.UserDtls;
import com.example.demo.repositary.DatabaseFileRepository;
import com.example.demo.repositary.UserRepositary;
import com.example.demo.service.PersonalService;
import com.example.demo.repositary.personalRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

	@Autowired
	private UserRepositary userRepo;

	@Autowired
	private DatabaseFileRepository fileRepo;

	@Autowired
	private PersonalService personalService;

	@Autowired
	private BCryptPasswordEncoder passwordEncode;

	@Autowired
	private personalRepository personalRepository;

	@ModelAttribute
	private void userDetails(Model model, Principal p) {
		Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);
		logger.info("\n\n\nWorking fine\n\n\n");

		if (p != null) {
			logger.info("\n\n\nPrincipal is not null\n\n\n");
			String email = p.getName();
			UserDtls user = userRepo.findByEmail(email);

			System.out.println("User details:");
			System.out.println("Name: " + user.getName());
			System.out.println("Email: " + user.getEmail());
			// System.out.println("Department: " + user.getDepartment());
			// and so on for all fields in the UserDtls entity

			logger.debug("\n\n\nId is: " + user.getId() + "\n\n\n");

			PersonalDtls puser = personalRepository.findById(user.getId());

			System.out.println("Personal details:");
			System.out.println("Address: " + puser.getCurAdd());
			System.out.println("Phone: " + puser.getCurrCity());
			System.out.println("City: " + puser.getCurrCunt());
			System.out.println("State: " + puser.getCurrState());
			System.out.println("DOB: " + puser.getDob());
			System.out.println("Gender: " + puser.getGender());
			logger.debug("\n\nPersonal details is: " + puser.toString());

			model.addAttribute("user", user);
			model.addAttribute("puser", puser);

		} else {
			logger.debug("Principal is null");
		}

		logger.info("\n\n\nDoc details are working fine\n\n\n");

		String email = p.getName();
		UserDtls user = userRepo.findByEmail(email);

		// log user details
		System.out.println("User details:");
		System.out.println("Name: " + user.getName());
		System.out.println("Email: " + user.getEmail());
		System.out.println("Id: " + user.getId());

		List<DatabaseFile> files = fileRepo.findByUser(user);
		System.out.println("Files: " + files);

		model.addAttribute("files", files);

		// log files info
		for (DatabaseFile file : files) {
			logger.info(" File: " + file.getFileName());
		}

	}

	@GetMapping("/files")
	public String listFiles(Model model, Principal principal) {
		/*
		 * to use the above mapping in a web page, use the following code
		 * <table>
		 * <thead>
		 * <tr>
		 * <th>Name</th>
		 * <th>Type</th>
		 * <th>Actions</th>
		 * </tr>
		 * </thead>
		 * <tbody>
		 * <tr th:each="file : ${files}">
		 * <td th:text="${file.fileName}"></td>
		 * <td th:text="${file.fileType}"></td>
		 * <td>
		 * <a th:href="@{/download/{id}(id=${file.id})}"
		 * class="btn btn-primary">Download</a>
		 * <span th:if="${file.user.id == currentUser.id}">
		 * <a href="#" class="btn btn-primary" data-toggle="modal"
		 * data-target="#uploadModal">Upload New Version</a>
		 * </span>
		 * </td>
		 * </tr>
		 * </tbody>
		 * </table>
		 * 
		 */
		UserDtls user = userRepo.findByEmail(principal.getName());
		List<DatabaseFile> files = user.getFiles();
		model.addAttribute("files", files);
		return "files";
	}

	@GetMapping("/")
	public String home() {
		return "user/teacherFiles/teacher";
	}

	@GetMapping("/teacher-dashboard")
	public String dashboard() {
		return "user/teacherFiles/teacherDashboard";
	}

	@GetMapping("/personalInfo")
	public String personalInfo() {
		return "user/teacherFiles/personalInfo";
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

	@GetMapping("/settings")
	public String settings() {
		return "user/teacherFiles/settings";
	}

	@GetMapping("/changePass")
	public String loadChangePassword() {
		return "user/change_password";
	}

	@GetMapping("/update-user-details")
	public String updateDetails() {
		return "user/teacherFiles/detailUpdateForm";
	}

	@PostMapping("/personalUser")
	public String personalUser(@ModelAttribute PersonalDtls puser) {
		PersonalDtls personalDtls = personalService.personalUser(puser);

		if (personalDtls != null) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "user/teacherFiles/personalInfo";
		}
		return "user/teacherFiles/detailUpdateForm";

	}

	@PostMapping("/updatePassword")
	public String changePassword(Principal p, @RequestParam("oldPass") String oldPass,
			@RequestParam("newPass") String newPass, HttpSession session) {

		String email = p.getName();
		UserDtls loginUser = userRepo.findByEmail(email);

		boolean f = passwordEncode.matches(oldPass, loginUser.getPassword());

		if (f) {
			loginUser.setPassword(passwordEncode.encode(newPass));
			UserDtls updatePasswordUser = userRepo.save(loginUser);
			if (updatePasswordUser != null) {
				System.out.println("password changed successfully");
			} else {
				System.out.println("something went wrong");
			}

		} else {
			System.out.println("incorrect password");

		}

		return "redirect:/user/changePass";

	}
}