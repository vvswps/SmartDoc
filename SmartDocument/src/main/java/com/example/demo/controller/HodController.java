package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.DatabaseFile;
import com.example.demo.model.DatabaseFile.FileType;
import com.example.demo.model.PersonalDtls;
import com.example.demo.model.UserDtls;
import com.example.demo.repository.DatabaseFileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.personalRepository;
import com.example.demo.service.PersonalService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/hod")
public class HodController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private PersonalService personalService;

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private personalRepository personalRepository;

	@Autowired
	private DatabaseFileRepository fileRepo;

//	@ModelAttribute
//	private void userDetails(Model model, Principal p) {
//		if (p != null) {
//			String email = p.getName();
//			UserDtls user = userRepo.findByEmail(email);
//
//			model.addAttribute("user", user);
//		}
//
//	}
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
	
	
	
	
	@PostMapping("/getFacultyByEmail")
	public String getFacultyByEmail(@RequestParam String email, Model model,RedirectAttributes redirectAttributes, HttpSession session, Principal principal) {
	    System.out.println("In getFacultyByEmail()");

	    UserDtls hodUser = userRepo.findByEmail(principal.getName());
	    UserDtls facultyUser = userRepo.findByEmail(email);
	    if (facultyUser == null) {
	        // The faculty user does not exist
	        redirectAttributes.addFlashAttribute("error", "Invalid email");
	        return "redirect:/hod/facultyView";
	    }

	    // Check if the HOD and faculty belong to the same department
	    if (hodUser.getBranch().equals(facultyUser.getBranch())) {
	        session.setAttribute("email", email);

	        PersonalDtls puser = personalRepository.findById(facultyUser.getId());
	        model.addAttribute("puser", puser);

	        List<DatabaseFile> awardsFiles = new ArrayList<>();
	        List<DatabaseFile> achievementsFiles = new ArrayList<>();
	        List<DatabaseFile> researchFiles = new ArrayList<>();
	        List<DatabaseFile> bookFiles = new ArrayList<>();
	        List<DatabaseFile> fdpFiles = new ArrayList<>();
	        List<DatabaseFile> sttpFiles = new ArrayList<>();
	        List<DatabaseFile> qipFiles = new ArrayList<>();
	        List<DatabaseFile> workshopFiles = new ArrayList<>();

	        List<DatabaseFile> files = fileRepo.findByUser(facultyUser);

	        try {
	            for (DatabaseFile file : files) {
	                FileType fileType = file.getType();
	                System.out.println(file + "Type:\t" + fileType);
	                switch (fileType) {
	                    case AWARD:
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

	        return "user/admin/emailView";
	    } else {
	    	redirectAttributes.addFlashAttribute("error", "apna department dekh na");
			return "redirect:/hod/settings";
	    }
	}
	
	
	
	///upload
	@GetMapping("/files")
	public String listFiles(Model model, Principal principal) {
		
		UserDtls user = userRepo.findByEmail(principal.getName());
		List<DatabaseFile> files = user.getFiles();
		model.addAttribute("files", files);
		return "files";
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
			return "redirect:/hod/personalInfo";

		} catch (Exception e) {
			System.err.println("Error in updating user details\n\n\n\n");
			e.printStackTrace();
		}
		return "redirect:/hod/update-user-details";
	}

	
	
	
	
	
	@PostMapping(value = "/getFacultyByDept")
	public String getFacultyByDept(@RequestParam("department") String dept, Model model,RedirectAttributes redirectAttributes, HttpSession session, Principal principal) {
	    System.out.println("\n\n\nIn getFacultyByDept()\n\n\n");

	    UserDtls hodUser = userRepo.findByEmail(principal.getName());

	    // Check if the HOD's department matches the requested department
	    if (hodUser.getBranch().equals(dept)) {
	        List<UserDtls> teachers = userRepo.findByBranchAndRole(dept, "ROLE_TEACHER");
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

	        System.out.println("\n\nTeacher file counts!!!!!!!!!!!!!!!!!!!!!!!");
	        System.out.println(teacherFileCounts);

	        model.addAttribute("teacherFileCounts", teacherFileCounts);

	        return "user/admin/deptView";
	    } else {
	    	redirectAttributes.addFlashAttribute("error", "apna department dekh na");
			return "redirect:/hod/facultyView";
//	        // HOD's department does not match the requested department, handle the error or redirection here
//	        return "errorPage"; // Replace with the appropriate error handling or redirection logic
	    }
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
	@GetMapping("/facultyView")
	public String facultyView() {
		return "user/admin/faculty";
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
			System.out.println("ji");
			UserDtls updatePasswordUser = userRepo.save(loginUser);
			if (updatePasswordUser != null) {
				System.out.println("password changed successfully");
			} else {
				System.out.println("something went wrong");
			}

		} else {
			System.out.println("incorrect password");

		}

		return "redirect:/hod/settings";

	}


}
