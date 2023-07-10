package com.example.demo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import com.example.demo.model.DatabaseFile.FileType;
import com.example.demo.repository.DatabaseFileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.personalRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder passwordEncode;

	@Autowired
	private personalRepository personalRepository;

	@Autowired
	private DatabaseFileRepository fileRepo;

	@ModelAttribute
	private void userDetails(Model model, Principal p) {
		if (p != null) {
			String email = p.getName();
			UserDtls user = userRepo.findByEmail(email);

			model.addAttribute("user", user);
		}

	}

	@PostMapping("/getFacultyByEmail")
	public String getFacultyByEmail(@RequestParam String email, Model model, HttpSession session) {
		System.out.println("In getFacultyByEmail()");
		UserDtls user = userRepo.findByEmail(email);

		// model.addAttribute("email", email);
		session.setAttribute("email", email);

		PersonalDtls puser = personalRepository.findById(user.getId());
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
	}

	@GetMapping("/downloadCSV")
	public void downloadCSV(HttpServletResponse response, HttpSession session) throws IOException {
		System.out.println("In downloadCSV()");

		String email = (String) session.getAttribute("email");
		System.out.println("Email:\t" + email);
		UserDtls user = userRepo.findByEmail(email);
		PersonalDtls puser = personalRepository.findById(user.getId());

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
		generateCSV(puser, awardsFiles, achievementsFiles, researchFiles, bookFiles, fdpFiles, sttpFiles, qipFiles,
				workshopFiles, response);
	}

	private void generateCSV(PersonalDtls puser, List<DatabaseFile> awardsFiles, List<DatabaseFile> achievementsFiles,
			List<DatabaseFile> researchFiles, List<DatabaseFile> bookFiles, List<DatabaseFile> fdpFiles,
			List<DatabaseFile> sttpFiles, List<DatabaseFile> qipFiles, List<DatabaseFile> workshopFiles,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"user_details.csv\"");
		PrintWriter writer = response.getWriter();

		writer.println("User details");
		writer.println(
				"Name, ERP id, Official Email, Personal Email, Department, Whatsapp Number, Mobile Number, Gender, Date of Birth, Industry Experience, Academic Experience, Date of Joining, Date of Leaving, Google Scholar ID, Scopus ID, SCI ID, Current Address, Current City, Current State, Current Country, Current Pincode, Permanent Address, Permanent City, Permanent State, Permanent Country, Permanent Pincode");
		writer.println(puser.getName() + "," + puser.getErpId() + "," + puser.getOffEmail() + "," + puser.getPerEmail()
				+ "," + puser.getDept() + "," + puser.getWhatsNumber() + "," + puser.getMobileNumber() + ","
				+ puser.getGender() + "," + puser.getDob() + "," + puser.getErpId() + "," + puser.getExpAcd() + ","
				+ puser.getDoj() + "," + puser.getDol() + "," + puser.getGoogleId() + "," + puser.getScopusId() + ","
				+ puser.getSciId() + "," + puser.getCurAdd() + "," + puser.getCurrCity() + "," + puser.getCurrState()
				+ "," + puser.getCurrCunt() + "," + puser.getCurrPin() + "," + puser.getPerAdd() + ","
				+ puser.getPerCity() + "," + puser.getPerState() + "," + puser.getPerCunt() + "," + puser.getPerPin());

		writer.println("\n\n");
		writer.println("Research papers");

		for (DatabaseFile file : researchFiles) {
			writer.println(
					"Title, Publication Name, Type of Publication, Date of Publishing, ISSN Number, DOI, Volume");
			writer.println(file.getTitle() + "," + file.getPublicationName() + "," + file.getPublicationType() + ","
					+ file.getDate() + "," + file.getISSN() + "," + file.getDOI() + ","
					+ file.getVolume());
		}

		writer.println("\n\n");
		writer.println("Books and Chapters");

		for (DatabaseFile file : bookFiles) {
			writer.println(
					"Title, Publication Name, Type of Publication, Date of Publishing, ISBN Number");
			writer.println(file.getTitle() + "," + file.getPublicationName() + "," + file.getPublicationType() + ","
					+ file.getDate() + "," + file.getISBN());
		}

		writer.println("\n\n");
		writer.println("FDPs");

		for (DatabaseFile file : fdpFiles) {
			writer.println(
					"Title, Online/Offline, Number of days, Organization");
			writer.println(file.getTitle() + "," + file.getNature() + ","
					+ file.getNoOfDays() + "," + file.getOrganizedBy());
		}

		writer.println("\n\n");
		writer.println("STTPs");

		for (DatabaseFile file : sttpFiles) {
			writer.println(
					"Title, Online/Offline, Number of days, Organization");
			writer.println(file.getTitle() + "," + file.getNature() + ","
					+ file.getNoOfDays() + "," + file.getOrganizedBy());
		}

		writer.println("\n\n");
		writer.println("QIPs");

		for (DatabaseFile file : qipFiles) {
			writer.println(
					"Title, Online/Offline, Number of days, Organization");
			writer.println(file.getTitle() + "," + file.getNature() + ","
					+ file.getNoOfDays() + "," + file.getOrganizedBy());
		}

		writer.println("\n\n");
		writer.println("Workshops");

		for (DatabaseFile file : workshopFiles) {
			writer.println(
					"Title, Online/Offline, Number of days, Organization");
			writer.println(file.getTitle() + "," + file.getNature() + ","
					+ file.getNoOfDays() + "," + file.getOrganizedBy());
		}

		writer.flush();
		writer.close();
	}

	@PostMapping(value = "/getFacultyByDept")
	public String getFacultyByDept(@RequestParam("department") String dept, Model model, HttpSession session) {
		System.out.println("\n\n\nIn getFacultyByDept()\n\n\n");
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
	}

	@GetMapping("/downloadDeptCSV")
	public void downloadDeptCSV(HttpServletResponse response, HttpSession session) throws IOException {
		String deptName = (String) session.getAttribute("deptName");
		List<UserDtls> teachers = userRepo.findByBranchAndRole(deptName, "ROLE_TEACHER");
		Map<String, Map<FileType, Integer>> teacherFileCounts = new HashMap<>();

		for (UserDtls teacher : teachers) {
			Map<FileType, Integer> fileCounts = new HashMap<>();
			for (FileType fileType : FileType.values()) {
				fileCounts.put(fileType, 0);
			}
			for (DatabaseFile file : teacher.getFiles()) {
				FileType fileType = file.getType();
				fileCounts.put(fileType, fileCounts.get(fileType) + 1);
			}
			teacherFileCounts.put(teacher.getName(), fileCounts);
		}

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + deptName + ".csv\"");

		try (CSVPrinter csvPrinter = new CSVPrinter(response.getWriter(),
				CSVFormat.DEFAULT.withHeader("Teacher Name", "Email", "Research Papers", "Awards", "Achievements",
						"Books or Chapters", "FDP", "STTP", "QIP", "Workshop")
						.withQuoteMode(QuoteMode.ALL))) {
			generateDeptCSV(csvPrinter, teachers, teacherFileCounts);
		}
	}

	private void generateDeptCSV(CSVPrinter csvPrinter, List<UserDtls> teachers,
			Map<String, Map<FileType, Integer>> teacherFileCounts) throws IOException {
		for (UserDtls teacher : teachers) {
			csvPrinter.printRecord(
					teacher.getName(),
					teacher.getEmail(),
					teacherFileCounts.get(teacher.getName()).getOrDefault(FileType.RESEARCH_PAPER, 0),
					teacherFileCounts.get(teacher.getName()).getOrDefault(FileType.AWARD, 0),
					teacherFileCounts.get(teacher.getName()).getOrDefault(FileType.ACHIEVEMENT, 0),
					teacherFileCounts.get(teacher.getName()).getOrDefault(FileType.BOOK_OR_CHAPTER, 0),
					teacherFileCounts.get(teacher.getName()).getOrDefault(FileType.FDP, 0),
					teacherFileCounts.get(teacher.getName()).getOrDefault(FileType.STTP, 0),
					teacherFileCounts.get(teacher.getName()).getOrDefault(FileType.QIP, 0),
					teacherFileCounts.get(teacher.getName()).getOrDefault(FileType.WORKSHOP, 0));
		}
	}

	@GetMapping("/")
	public String home() {
		return "user/admin/admin";
	}

	@GetMapping("/permissions")
	public String permissions(@RequestParam("dept") String dept, Model model) {
		List<UserDtls> teachers = userRepo.findByRole("ROLE_TEACHER");

		model.addAttribute("teachers", teachers);
		return "user/admin/permissions";
	}

	@GetMapping("/adminDashboard")
	public String adminDashboard() {
		return "user/admin/admin_dashboard";
	}

	@GetMapping("/facultyView")
	public String facultyView() {
		return "user/admin/faculty";
	}

	@GetMapping("/studentView")
	public String studentView() {
		return "user/admin/student";
	}

	@GetMapping("/deptView")
	public String deptView() {
		return "user/admin/deptView";
	}

	@GetMapping("/mailView")
	public String mailView() {
		return "user/admin/emailView";
	}

	@GetMapping("/settings")
	public String settings() {
		return "user/teacherFiles/settings";
	}

	@GetMapping("/changePass")
	public String loadChangePassword() {
		return "user/change_password";
	}

	@PostMapping("/updatePassword")
	public String changePassword(Principal p, @RequestParam String oldPass,
			@RequestParam String newPass, HttpSession session) {

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

		return "redirect:/admin/settings";

	}

}