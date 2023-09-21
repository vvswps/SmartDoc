package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
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
import com.example.demo.model.DatabaseFile.FileType;
import com.example.demo.model.PersonalDtls;
import com.example.demo.model.UserDtls;
import com.example.demo.repository.DatabaseFileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.personalRepository;
import com.example.demo.service.FileUtils;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/hod")
public class HodController {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private personalRepository personalRepository;

	@Autowired
	private DatabaseFileRepository fileRepo;

	@Autowired
	private FileUtils fileUtils;

	String reset = "\u001B[0m";
	String red = "\u001B[31m";
	String green = "\u001B[32m";
	String yellow = "\u001B[33m";
	String blue = "\u001B[34m";
	String cyan = "\u001B[36m";

	@ModelAttribute
	private void userDetails(Model model, Principal p) {
		if (p != null) {
			String email = p.getName();
			UserDtls user = userRepo.findByEmail(email);
			model.addAttribute("user", user);
			PersonalDtls personalDtls = personalRepository.findByUser(user);
			model.addAttribute("personalDetails", personalDtls);

			// personalDtls + reset);

			// reset);
			fileUtils.populateFileListsAndAddToModel(user, model);

			// model + reset);

		} else {
			model.addAttribute("user", null);
		}
	}

	@ModelAttribute("fileTypes")
	public String[] getFileTypes() {
		return Arrays.stream(FileType.values()).filter(fileType -> fileType != FileType.PROFILE_PICTURE)
				.map(fileType -> fileType.toString().replaceAll("_", " "))
				.toArray(String[]::new);
	}

	@PostMapping("/getFacultyByEmail")
	public String getFacultyByEmail(@RequestParam String email, Model model, HttpSession session) {

		UserDtls user = userRepo.findByEmail(email);
		String facultyProfilePicId = "";
		try {
			facultyProfilePicId = fileRepo.findByUserAndType(user, FileType.PROFILE_PICTURE).get(0).getId();

		} catch (IndexOutOfBoundsException e) {

		}
		model.addAttribute("facultyProfilePicId", facultyProfilePicId);
		// model.addAttribute("email", email);
		session.setAttribute("email", email);

		// here user is the faculty
		fileUtils.populateFileListsAndAddToModel(user, model);

		return "user/admin/emailView";
	}

	/// upload
	@GetMapping("/files")
	public String listFiles(Model model, Principal principal) {

		UserDtls user = userRepo.findByEmail(principal.getName());
		List<DatabaseFile> files = user.getFiles();
		model.addAttribute("files", files);
		return "files";
	}

	@GetMapping("/detailUpdateForm")
	public String updateDetails(Model model) {

		return "user/teacherFiles/detailUpdateForm";
	}

	@PostMapping("/update-user-details")
	public String updateUser(@ModelAttribute("personalDetails") PersonalDtls updatedPersonalDtls) {

		try {
			personalRepository.save(updatedPersonalDtls);
			return "redirect:/teacher/personalInfo";

		} catch (Exception e) {
			System.err.println("Error in updating user details");
			e.printStackTrace();
		}
		return "redirect:/teacher/update-user-details";
	}

	@GetMapping("/deptView")
	public String facultyView(Model model, HttpSession session, Principal principal) {
		UserDtls hodUser = userRepo.findByEmail(principal.getName());
		String dept = hodUser.getBranch();
		List<UserDtls> teachers = userRepo.findByBranchAndRole(dept, "ROLE_TEACHER");
		teachers.add(0, hodUser);
		model.addAttribute("deptName", dept);
		session.setAttribute("deptName", dept);
		model.addAttribute("teachers", teachers);

		Map<String, Map<FileType, Integer>> teacherFileCounts = new HashMap<>();

		for (UserDtls teacher : teachers) {
			Map<FileType, Integer> fileCounts = new HashMap<>();
			for (FileType fileType : FileType.values()) {
				if (fileType == FileType.PROFILE_PICTURE)
					continue;

				fileCounts.put(fileType, 0);
			}
			for (DatabaseFile file : teacher.getFiles()) {
				FileType fileType = file.getType();

				if (fileType == FileType.PROFILE_PICTURE)
					continue;
				fileCounts.put(fileType, fileCounts.get(fileType) + 1);
			}
			teacherFileCounts.put(teacher.getName(), fileCounts);
		}

		model.addAttribute("teacherFileCounts", teacherFileCounts);

		return "user/admin/deptView";
	}

	@GetMapping("/")
	public String home() {
		return "user/hod/hod";
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

	@GetMapping("/update-user-details")
	public String updateDetails() {
		return "user/teacherFiles/detailUpdateForm";
	}

	@GetMapping("/fdp")
	public String fdp() {
		return "user/teacherFiles/fdp";
	}

	@GetMapping("/settings")
	public String settings() {
		return "user/teacherFiles/settings";
	}

	@GetMapping("/mailView")
	public String mailView() {
		return "user/admin/emailView";
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

			} else {

			}

		} else {

		}

		return "redirect:/hod/settings";

	}

}
