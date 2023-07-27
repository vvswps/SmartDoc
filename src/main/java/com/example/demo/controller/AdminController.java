package com.example.demo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import com.example.demo.service.FileUtils;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

@Controller
@RequestMapping("/admin")
public class AdminController {

	String reset = "\u001B[0m";
	String red = "\u001B[31m";
	String green = "\u001B[32m";
	String yellow = "\u001B[33m";
	String blue = "\u001B[34m";
	String cyan = "\u001B[36m";

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder passwordEncode;

	@Autowired
	private personalRepository personalRepository;

	@Autowired
	private DatabaseFileRepository fileRepo;

	@Autowired
	private FileUtils fileUtils;

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
		String facultyProfilePicId = "";
		try {
			facultyProfilePicId = fileRepo.findByUserAndType(user, FileType.PROFILE_PICTURE).get(0).getId();
			System.out.println(cyan + "Faculty Profile Pic:\t" + facultyProfilePicId + reset);
		} catch (IndexOutOfBoundsException e) {
			System.out.println(red + "No profile pic found" + reset);
		}
		model.addAttribute("facultyProfilePicId", facultyProfilePicId);
		// model.addAttribute("email", email);
		session.setAttribute("email", email);

		// here user is the faculty
		fileUtils.populateFileListsAndAddToModel(user, model);

		return "user/admin/emailView";
	}

	@GetMapping("/downloadCSV")
	public void downloadCSV(HttpServletResponse response, HttpSession session) throws IOException {
		System.out.println("In downloadCSV()");

		String email = (String) session.getAttribute("email");
		System.out.println(yellow + "Email:\t" + email + reset);
		UserDtls user = userRepo.findByEmail(email);

		PersonalDtls personalDetails = personalRepository.findByUser(user);
		System.out.println(red + "\n\nUser:\t" + user + reset);
		System.out.println(red + "\n\nPersonal User:\t" + personalDetails + reset);

		List<DatabaseFile> awardsFiles = new ArrayList<>();
		List<DatabaseFile> patentFiles = new ArrayList<>();
		List<DatabaseFile> researchFiles = new ArrayList<>();
		List<DatabaseFile> bookFiles = new ArrayList<>();
		List<DatabaseFile> fdpFiles = new ArrayList<>();
		List<DatabaseFile> sttpFiles = new ArrayList<>();
		List<DatabaseFile> qipFiles = new ArrayList<>();
		List<DatabaseFile> conference_workshop_seminar_Files = new ArrayList<>();
		List<DatabaseFile> industrialVisitsFiles = new ArrayList<>();
		List<DatabaseFile> guestLectureFiles = new ArrayList<>();

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
					case PATENT:
						patentFiles.add(file);
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
					case CONFERENCE_WORKSHOP_SEMINAR:
						conference_workshop_seminar_Files.add(file);
						break;
					case INDUSTRIALVISIT:
						industrialVisitsFiles.add(file);
						break;
					case GUESTLECTURE:
						guestLectureFiles.add(file);
						break;

					default:
						System.out.println("Unknown file type");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		generateCSV(personalDetails, awardsFiles, patentFiles, researchFiles, bookFiles, fdpFiles, sttpFiles, qipFiles,
				conference_workshop_seminar_Files, response);
	}

	private void generateCSV(PersonalDtls personalDetails, List<DatabaseFile> awardsFiles,
			List<DatabaseFile> achievementsFiles,
			List<DatabaseFile> researchFiles, List<DatabaseFile> bookFiles, List<DatabaseFile> fdpFiles,
			List<DatabaseFile> sttpFiles, List<DatabaseFile> qipFiles, List<DatabaseFile> workshopFiles,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"user_details.csv\"");
		PrintWriter writer = response.getWriter();

		writer.println("User details");
		writer.println(
				"Name, ERP id, Official Email, Personal Email, Department, Whatsapp Number, Mobile Number, Gender, Date of Birth, Industry Experience, Academic Experience, Date of Joining, Date of Leaving, Google Scholar ID, Scopus ID, SCI ID, Current Address, Current City, Current State, Current Country, Current Pincode, Permanent Address, Permanent City, Permanent State, Permanent Country, Permanent Pincode");
		writer.println(personalDetails.getName() + "," + personalDetails.getErpId() + ","
				+ personalDetails.getOffEmail() + "," + personalDetails.getPerEmail()
				+ "," + personalDetails.getDept() + "," + personalDetails.getWhatsNumber() + ","
				+ personalDetails.getMobileNumber() + ","
				+ personalDetails.getGender() + "," + personalDetails.getDob() + "," + personalDetails.getErpId() + ","
				+ personalDetails.getExpAcd() + ","
				+ personalDetails.getDoj() + "," + personalDetails.getDol() + "," + personalDetails.getGoogleId() + ","
				+ personalDetails.getScopusId() + ","
				+ personalDetails.getSciId() + "," + personalDetails.getCurAdd() + "," + personalDetails.getCurrCity()
				+ "," + personalDetails.getCurrState()
				+ "," + personalDetails.getCurrCunt() + "," + personalDetails.getCurrPin() + ","
				+ personalDetails.getPerAdd() + ","
				+ personalDetails.getPerCity() + "," + personalDetails.getPerState() + ","
				+ personalDetails.getPerCunt() + "," + personalDetails.getPerPin());

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

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + deptName + ".csv\"");

		try (CSVPrinter printer = new CSVPrinter(response.getWriter(), CSVFormat.DEFAULT)) {
			printer.printRecord("Department of " + deptName);
			printer.printRecord("Generated on: " + LocalDate.now().toString());
			printer.println();

			printer.printRecord("Section: Faculty Awards/Achievements");
			printer.println();
			printer.printRecord("Sr. No.", "Name of Faculty", "Email",
					"Award/Achievement Name", "Date Awarded", "Issuing Authority");

			int count = 0;
			printer.println();
			for (UserDtls teacher : teachers) {
				for (DatabaseFile file : teacher.getFiles()) {
					if (file.getType() == FileType.AWARD) {
						printer.print(++count);
						printer.print(teacher.getName());
						printer.print(teacher.getEmail());
						printer.print(file.getTitle());
						printer.print(file.getDate());
						printer.print(file.getAwardingInstitution());
						printer.println();
					}
				}
			}

			printer.println();
			printer.print("Section: Faculty Research Papers");
			printer.println();

			printer.printRecord("Sr. No.", "Name of Faculty", "Email",
					"Title of Paper", "Publication Name", "Type of Publication", "Date of Publishing", "ISSN Number",
					"DOI",
					"Volume");

			printer.println();

			count = 0;
			// "Sr. No.", "Name of Faculty", "Email", "Title of Paper", "Publication Name",
			// "Type of Publication", "Date of Publishing", "ISSN Number", "DOI", "Volume"
			for (UserDtls teacher : teachers) {
				for (DatabaseFile file : teacher.getFiles()) {
					if (file.getType() == FileType.RESEARCH_PAPER) {
						printer.print(++count);
						printer.print(teacher.getName());
						printer.print(teacher.getEmail());
						printer.print(file.getTitle());
						printer.print(file.getPublicationName());
						printer.print(file.getPublicationType());
						printer.print(file.getDate());
						printer.print(file.getISSN());
						printer.print(file.getDOI());
						printer.print(file.getVolume());
						printer.println();
					}
				}
			}

			printer.println();

			printer.print("Section: Faculty Books and Chapters");
			printer.println();
			printer.printRecord("Sr. No.", "Name of Faculty", "Email", "Title",
					"Publication Name", "Book/Chapter", "Date of Publishing", "ISBN Number");

			printer.println();

			count = 0;
			// "Sr. No.", "Name of Faculty", "Email", "Title", "Publication Name",
			// "Book/Chapter", "Date of Publishing", "ISBN Number"
			for (UserDtls teacher : teachers) {
				for (DatabaseFile file : teacher.getFiles()) {
					if (file.getType() == FileType.BOOK_OR_CHAPTER) {
						printer.print(++count);
						printer.print(teacher.getName());
						printer.print(teacher.getEmail());
						printer.print(file.getTitle());
						printer.print(file.getPublicationName());
						printer.print(file.getPublicationType());
						printer.print(file.getDate());
						printer.print(file.getISBN());
						printer.println();
					}
				}
			}

			printer.println();

			printer.print("Section: Faculty FDPs");
			printer.println();
			printer.printRecord("Sr. No.", "Name of Faculty", "Email", "Title",
					"Online/Offline", "Number of days", "Organization");

			printer.println();

			count = 0;
			// "Sr. No.", "Name of Faculty", "Email", "Title", "Online/Offline", "Number of
			// days", "Organization"
			for (UserDtls teacher : teachers) {
				for (DatabaseFile file : teacher.getFiles()) {
					if (file.getType() == FileType.FDP) {
						printer.print(++count);
						printer.print(teacher.getName());
						printer.print(teacher.getEmail());
						printer.print(file.getTitle());
						printer.print(file.getNature());
						printer.print(file.getNoOfDays());
						printer.print(file.getOrganizedBy());
						printer.println();
					}
				}
			}

			printer.println();
			printer.print("Section: Faculty STTPs");
			printer.println();
			printer.printRecord("Sr. No.", "Name of Faculty", "Email", "Title",
					"Online/Offline", "Number of days", "Organization");

			printer.println();

			count = 0;
			// "Sr. No.", "Name of Faculty", "Email", "Title", "Online/Offline", "Number of
			// days", "Organization"
			for (UserDtls teacher : teachers) {
				for (DatabaseFile file : teacher.getFiles()) {
					if (file.getType() == FileType.STTP) {
						printer.print(++count);
						printer.print(teacher.getName());
						printer.print(teacher.getEmail());
						printer.print(file.getTitle());
						printer.print(file.getNature());
						printer.print(file.getNoOfDays());
						printer.print(file.getOrganizedBy());
						printer.println();
					}
				}
			}

			printer.println();

			printer.print("Section: Faculty QIPs");
			printer.println();
			printer.printRecord("Sr. No.", "Name of Faculty", "Email", "Title",
					"Online/Offline", "Number of days", "Organization");

			printer.println();

			count = 0;
			// "Sr. No.", "Name of Faculty", "Email", "Title", "Online/Offline", "Number of
			// days", "Organization"
			for (UserDtls teacher : teachers) {
				for (DatabaseFile file : teacher.getFiles()) {
					if (file.getType() == FileType.QIP) {
						printer.print(++count);
						printer.print(teacher.getName());
						printer.print(teacher.getEmail());
						printer.print(file.getTitle());
						printer.print(file.getNature());
						printer.print(file.getNoOfDays());
						printer.print(file.getOrganizedBy());
						printer.println();
					}
				}
			}

			printer.println();

			printer.print("Section: Faculty Workshops");
			printer.println();
			printer.printRecord("Sr. No.", "Name of Faculty", "Email", "Title",
					"Type", "Organiser/Attendee", "Online/Offline", "Number of days", "Organization");
			printer.println();

			count = 0;
			// "Sr. No.", "Name of Faculty", "Email", "Title", "Type", "Organiser/Attendee",
			// "Online/Offline", "Number of days", "Organization"
			for (UserDtls teacher : teachers) {
				for (DatabaseFile file : teacher.getFiles()) {
					if (file.getType() == FileType.CONFERENCE_WORKSHOP_SEMINAR) {
						printer.print(++count);
						printer.print(teacher.getName());
						printer.print(teacher.getEmail());
						printer.print(file.getTitle());
						printer.print(file.getNature());
						printer.print(file.getOrganizedBy());
						printer.print(file.getNoOfDays());
						printer.print(file.getOrganizedBy());
						printer.println();
					}
				}
			}

			printer.println();
			printer.print("Section: Faculty Industrial Visits");
			printer.println();
			printer.printRecord("Sr. No.", "Name of Faculty", "Email",
					"Name of industry visited & Place", "No. of students visited", "Date of visit");
			printer.println();

			count = 0;
			// "Sr. No.", "Name of Faculty", "Email", "Name of industry visited & Place",
			// "No. of students visited", "Date of visit"
			for (UserDtls teacher : teachers) {
				for (DatabaseFile file : teacher.getFiles()) {
					if (file.getType() == FileType.INDUSTRIALVISIT) {
						printer.print(++count);
						printer.print(teacher.getName());
						printer.print(teacher.getEmail());
						printer.print(file.getTitle());
						printer.print(file.getNoOfStudentsVisited());
						printer.print(file.getDate());
						printer.println();
					}
				}
			}

			printer.println();
			printer.print("Section: Faculty Guest Lectures");
			printer.println();
			printer.printRecord("Sr. No.", "Name of Faculty", "Email",
					"Topic of lecture", "Online/Offline", "Date", "Place/Event where the lecture was delivered");
			printer.println();

			count = 0;
			// "Sr. No.", "Name of Faculty", "Email", "Topic of lecture", "Online/Offline",
			// "Date", "Place/Event where the lecture was delivered"
			for (UserDtls teacher : teachers) {
				for (DatabaseFile file : teacher.getFiles()) {
					if (file.getType() == FileType.GUESTLECTURE) {
						printer.print(++count);
						printer.print(teacher.getName());
						printer.print(teacher.getEmail());
						printer.print(file.getTitle());
						printer.print(file.getNature());
						printer.print(file.getDate());
						printer.print(file.getOrganizedBy());
						printer.println();
					}
				}
			}

			printer.println();
			printer.print("Section: Faculty Patents");
			printer.println();
			printer.printRecord("Sr. No.", "Name of Faculty", "Email", "Title",
					"Patent Number", "Date of Patent", "Status");
			printer.println();

			count = 0;
			// "Sr. No.", "Name of Faculty", "Email", "Title", "Patent Number", "Date of
			// Patent", "Status"
			for (UserDtls teacher : teachers) {
				for (DatabaseFile file : teacher.getFiles()) {
					if (file.getType() == FileType.PATENT) {
						printer.print(++count);
						printer.print(teacher.getName());
						printer.print(teacher.getEmail());
						printer.print(file.getTitle());
						printer.print(file.getPatentNumber());
						printer.print(file.getDate());
						printer.print(file.getPatentStatus());
						printer.println();
					}
				}
			}

			printer.flush();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@GetMapping("/permissions")
	public String permissions(Model model, HttpSession session) {
		String dept = (String) session.getAttribute("deptName");
		model.addAttribute("deptName", dept);
		return "user/admin/permissions";
	}

	@PostMapping("/permissions")
	public String getPermissions(@RequestParam("department") String dept, Model model, HttpSession session) {
		List<UserDtls> teachers = userRepo.findByBranch(dept);
		model.addAttribute("deptName", dept);
		session.setAttribute("deptName", dept);
		model.addAttribute("teachers", teachers);
		return "user/admin/permissions";
	}

	@PostMapping("/updateRole")
	public String updateRole(@RequestParam("teacherId") int teacherId, @RequestParam("role") String role) {
		// Retrieve the teacher by teacherId from the database
		// Check if the role is either ROLE_TEACHER or ROLE_HOD so that the admin cannot
		// change the role of the admin or any other non existing role for that matter :
		// tu padhri isko :o
		Set<String> allowedRoles = new HashSet<>(Arrays.asList("ROLE_TEACHER", "ROLE_HOD"));

		Optional<UserDtls> optionalTeacher = userRepo.findById(teacherId);
		if (optionalTeacher.isPresent() && allowedRoles.contains(role)) {
			UserDtls teacher = optionalTeacher.get();
			// Update the role for the teacher
			teacher.setRole(role);
			// Save the updated teacher to the database
			userRepo.save(teacher);
		}
		return "redirect:/admin/permissions";
	}

	@GetMapping("/")
	public String home() {
		return "user/admin/admin";
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

	@ControllerAdvice
	public class GlobalExceptionHandler {

		@ExceptionHandler(Exception.class)
		public ResponseEntity<String> handleException(Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
		}
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
