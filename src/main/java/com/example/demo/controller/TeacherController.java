package com.example.demo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.DatabaseFile;
import com.example.demo.model.PersonalDtls;
import com.example.demo.model.UserDtls;
import com.example.demo.model.DatabaseFile.FileType;
import com.example.demo.repository.DatabaseFileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.personalRepository;
import com.example.demo.service.PersonalService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private DatabaseFileRepository fileRepo;

	@Autowired
	private PersonalService personalService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private personalRepository personalRepository;

	@ModelAttribute
	private void userDetails(Model model, Principal p) {
		if (p != null) {
			String email = p.getName();
			UserDtls user = userRepo.findByEmail(email);

			PersonalDtls puser = personalRepository.findById(user.getId());
			// PersonalDtls puser = personalRepository.findByUser(user);

			model.addAttribute("user", user);
			model.addAttribute("puser", puser);

			List<DatabaseFile> awardsFiles = new ArrayList<>();
			List<DatabaseFile> achievementsFiles = new ArrayList<>();
			List<DatabaseFile> researchFiles = new ArrayList<>();
			List<DatabaseFile> bookFiles = new ArrayList<>();
			List<DatabaseFile> fdpFiles = new ArrayList<>();
			List<DatabaseFile> sttpFiles = new ArrayList<>();
			List<DatabaseFile> qipFiles = new ArrayList<>();
			List<DatabaseFile> workshopFiles = new ArrayList<>();

			List<DatabaseFile> files = fileRepo.findByUser(user);
			try {
				for (DatabaseFile file : files) {
					FileType fileType = file.getType();
					// System.out.println(file + "Type:\t" + fileType);
					switch (fileType) {
						case AWARD:
							// System.out.println("File Type is Award");
							awardsFiles.add(file);
							break;
						case ACHIEVEMENT:
							achievementsFiles.add(file);
							break;
						case RESEARCH_PAPER:
							researchFiles.add(file);
							break;
						case BOOK_OR_CHAPTER:
							bookFiles.add(file);
							break;
						case FDP:
							fdpFiles.add(file);
							break;
						case STTP:
							sttpFiles.add(file);
							break;
						case QIP:
							qipFiles.add(file);
							break;
						case WORKSHOP:
							workshopFiles.add(file);
							break;
						default:
							System.out.println("Unknown file type");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!awardsFiles.isEmpty()) {
				// System.out.println("Awards Files are not empty");
				model.addAttribute("awardsFiles", awardsFiles);

			}
			if (!achievementsFiles.isEmpty()) {
				model.addAttribute("achievementsFiles", achievementsFiles);
			}

			if (!researchFiles.isEmpty()) {
				model.addAttribute("researchFiles", researchFiles);
			}

			if (!bookFiles.isEmpty()) {
				model.addAttribute("bookFiles", bookFiles);
			}

			if (!fdpFiles.isEmpty()) {
				model.addAttribute("fdpFiles", fdpFiles);
			}

			if (!sttpFiles.isEmpty()) {
				model.addAttribute("sttpFiles", sttpFiles);
			}

			if (!qipFiles.isEmpty()) {
				model.addAttribute("qipFiles", qipFiles);
			}

			if (!workshopFiles.isEmpty()) {
				model.addAttribute("workshopFiles", workshopFiles);
			}
		} else {
			model.addAttribute("user", null);
		}

	}

	@PostMapping("/updateTeacher")
	public String updateUser(Principal p,
			@RequestParam(required = true) String name,
			@RequestParam(required = true) String erpId,
			@RequestParam(required = true) String offEmail,
			@RequestParam(required = true) String perEmail,
			@RequestParam(required = true) String dept,
			@RequestParam(required = true) String whatsNumber,
			@RequestParam(required = true) String mobileNumber,
			@RequestParam(required = true) String dob,
			@RequestParam(required = true) String gender,
			@RequestParam(required = false) String expInd,
			@RequestParam(required = false) String expAcd,
			@RequestParam(required = false) String doj,
			@RequestParam(required = false) String dol,
			@RequestParam(required = false) String googleId,
			@RequestParam(required = false) String scopusId,
			@RequestParam(required = false) String sciId,
			@RequestParam(required = true) String curAdd,
			@RequestParam(required = true) String currCity,
			@RequestParam(required = true) String currState,
			@RequestParam(required = true) String currCunt,
			@RequestParam(required = true) String currPin,
			@RequestParam(required = true) String perAdd,
			@RequestParam(required = true) String perCity,
			@RequestParam(required = true) String perState,
			@RequestParam(required = true) String perCunt,
			@RequestParam(required = true) String perPin) {

		String email = p.getName();
		UserDtls user = userRepo.findByEmail(email);
		try {
			PersonalDtls puser = personalRepository.findByUser(user);

			PersonalDtls existingPersonalDtls = personalRepository.findByUser(puser.getUser());

			// update the fields with the new values
			existingPersonalDtls.setName(name);
			existingPersonalDtls.setErpId(erpId);
			existingPersonalDtls.setOffEmail(offEmail);
			existingPersonalDtls.setPerEmail(perEmail);
			existingPersonalDtls.setDept(dept);
			existingPersonalDtls.setWhatsNumber(whatsNumber);
			existingPersonalDtls.setMobileNumber(mobileNumber);
			existingPersonalDtls.setGender(gender);
			existingPersonalDtls.setDob(dob);
			existingPersonalDtls.setExpInd(expInd);
			existingPersonalDtls.setExpAcd(expAcd);
			existingPersonalDtls.setDoj(doj);
			existingPersonalDtls.setDol(dol);
			existingPersonalDtls.setGoogleId(googleId);
			existingPersonalDtls.setScopusId(scopusId);
			existingPersonalDtls.setSciId(sciId);
			existingPersonalDtls.setCurAdd(curAdd);
			existingPersonalDtls.setCurrCity(currCity);
			existingPersonalDtls.setCurrState(currState);
			existingPersonalDtls.setCurrCunt(currCunt);
			existingPersonalDtls.setCurrPin(currPin);
			existingPersonalDtls.setPerAdd(perAdd);
			existingPersonalDtls.setPerCity(perCity);
			existingPersonalDtls.setPerState(perState);
			existingPersonalDtls.setPerCunt(perCunt);
			existingPersonalDtls.setPerPin(perPin);

			// save the updated personal details object
			personalRepository.save(existingPersonalDtls);
			return "redirect:/teacher/personalInfo";

		} catch (Exception e) {
			System.err.println("Error in updating user details\n\n\n\n");
			e.printStackTrace();
		}
		return "redirect:/teacher/update-user-details";
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