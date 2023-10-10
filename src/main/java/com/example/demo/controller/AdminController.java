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

import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
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

	Logger logger;

	@ModelAttribute
	private void userDetails(Model model, Principal p) {
		if (p != null) {
			String email = p.getName();
			UserDtls user = userRepo.findByEmail(email);

			model.addAttribute("user", user);
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

	@GetMapping("/downloadCSV")
	public void downloadCSV(HttpServletResponse response, HttpSession session) throws IOException {

		String email = (String) session.getAttribute("email");

		UserDtls user = userRepo.findByEmail(email);

		PersonalDtls personalDetails = personalRepository.findByUser(user);

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

				switch (fileType) {
					case AWARD:

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
		List<UserDtls> teachers = new ArrayList<>();
		try {
			teachers = userRepo.findByBranchAndRole(dept, "ROLE_TEACHER");
			System.out.println("\u001B[31m" + teachers + "\u001B[0m");
			teachers.add(0, userRepo.findByBranchAndRole(dept, "ROLE_HOD").get(0));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("\u001B[31m" + "No HOD/Teacher found" + "\u001B[0m");
		}
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
				System.out.println("\u100B" + file.getType() + "\u100B");

				if (fileType == FileType.PROFILE_PICTURE)
					continue;
				fileCounts.put(fileType, fileCounts.get(fileType) + 1);
			}
			teacherFileCounts.put(teacher.getName(), fileCounts);
		}

		System.out.println(blue + teacherFileCounts + reset);

		model.addAttribute("teacherFileCounts", teacherFileCounts);

		return "user/admin/deptView";

	}

	@PostMapping("/downloadDeptCSV")
	public void downloadDeptCSV(
			@RequestParam(value = "selectedFileTypes", required = false) List<String> selectedFileTypes,
			HttpServletResponse response, HttpSession session) throws IOException {

		String deptName = (String) session.getAttribute("deptName");
		List<UserDtls> teachers = userRepo.findByBranchAndRole(deptName, "ROLE_TEACHER");

		if (selectedFileTypes == null || selectedFileTypes.isEmpty()) {
			logger.warn("No file types selected by the user.");
			return;
		}
		Workbook workbook = new XSSFWorkbook();

		Sheet userDetailsSheet = workbook.createSheet("User details");

		// cell style for the title
		CellStyle titleStyle = workbook.createCellStyle();
		Font titleFont = workbook.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 16); // Set font size
		titleStyle.setFont(titleFont);
		titleStyle.setAlignment(HorizontalAlignment.CENTER); // Center-align text
		titleStyle.setVerticalAlignment(VerticalAlignment.CENTER); // Center-align vertically

		// Define cell style for headers
		CellStyle headerStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerStyle.setFont(headerFont);

		Row deptRow = userDetailsSheet.createRow(0);
		Cell deptCell = deptRow.createCell(0);
		deptCell.setCellValue(("Department of " + deptName));
		deptRow.createCell(1).setCellValue("Generated on: " + LocalDate.now().toString());

		Row userDetailsTitleRow = userDetailsSheet.createRow(0);
		Cell userDetailsTitleCell = userDetailsTitleRow.createCell(0);
		userDetailsTitleCell.setCellValue("User Details");
		userDetailsTitleCell.setCellStyle(titleStyle);
		userDetailsSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10)); // Merge cells for the title

		// Create header row
		userDetailsSheet.createRow(1)
				.createCell(0).setCellValue("Yet to be implemented");

		// Create all the sheets required
		Sheet awardsSheet = workbook.createSheet("Awards");
		Sheet researchSheet = workbook.createSheet("Research papers");
		Sheet booksSheet = workbook.createSheet("Books and Chapters");
		Sheet fdpSheet = workbook.createSheet("FDPs");
		Sheet sttpSheet = workbook.createSheet("STTPs");
		Sheet qipSheet = workbook.createSheet("QIPs");
		Sheet conferenceSheet = workbook.createSheet("Conferences, Workshops, and Seminars");
		Sheet industrialVisitsSheet = workbook.createSheet("Industrial Visits");
		Sheet guestLectureSheet = workbook.createSheet("Guest Lectures");
		Sheet patentSheet = workbook.createSheet("Patents");

		// Create header row and add title to each sheet
		ArrayList<Sheet> sheets = new ArrayList<>(
				Arrays.asList(awardsSheet, researchSheet, booksSheet, fdpSheet, sttpSheet, qipSheet, conferenceSheet,
						industrialVisitsSheet, guestLectureSheet, patentSheet));
		sheets.forEach(sheet -> {
			Row headerRow = sheet.createRow(0);
			Cell headerCell = headerRow.createCell(0);
			headerCell.setCellValue(sheet.getSheetName());
			headerCell.setCellStyle(titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10)); // Merge cells for the title
		});

		for (String fileType : selectedFileTypes) {
			switch (fileType.toUpperCase()) {
				case "AWARD":
					// Create header row
					Row headerRow = awardsSheet.createRow(1);
					headerRow.createCell(0).setCellValue("Sr. No");
					headerRow.createCell(1).setCellValue("Name of Faculty");
					headerRow.createCell(2).setCellValue("Email");
					headerRow.createCell(3).setCellValue("Award/Achievement Name");
					headerRow.createCell(4).setCellValue("Date Awarded");
					headerRow.createCell(5).setCellValue("Issuing Authority");

					// Apply header cell style
					headerRow.forEach(cell -> cell.setCellStyle(headerStyle));

					int rowCount = 2;

					for (UserDtls teacher : teachers) {
						for (DatabaseFile file : teacher.getFiles()) {
							if (file.getType() == FileType.AWARD) {
								System.out.println("\u100B" + file.getType() + "\u100B");
								Row dataRow = awardsSheet.createRow(rowCount++);
								dataRow.createCell(0).setCellValue(rowCount - 2);
								dataRow.createCell(1).setCellValue(teacher.getName());
								dataRow.createCell(2).setCellValue(teacher.getEmail());
								dataRow.createCell(3).setCellValue(file.getTitle());
								dataRow.createCell(4).setCellValue(file.getDate().toString()); // Assuming date is a
																								// LocalDate
								dataRow.createCell(5).setCellValue(file.getAwardingInstitution());
							}
						}
					}
					break;

				case "RESEARCH PAPER":
					// Create header row
					headerRow = researchSheet.createRow(1);
					headerRow.createCell(0).setCellValue("Sr. No");
					headerRow.createCell(1).setCellValue("Name of Faculty");
					headerRow.createCell(2).setCellValue("Email");
					headerRow.createCell(3).setCellValue("Title of Paper");
					headerRow.createCell(4).setCellValue("Publication Name");
					headerRow.createCell(5).setCellValue("Type of Publication");
					headerRow.createCell(6).setCellValue("Date of Publishing");
					headerRow.createCell(7).setCellValue("ISSN Number");
					headerRow.createCell(8).setCellValue("DOI");
					headerRow.createCell(9).setCellValue("Volume");

					// Apply header cell style
					headerRow.forEach(cell -> cell.setCellStyle(headerStyle));

					rowCount = 2;

					for (UserDtls teacher : teachers) {
						for (DatabaseFile file : teacher.getFiles()) {
							if (file.getType() == FileType.RESEARCH_PAPER) {
								System.out.println("\u100B" + file.getType() + "\u100B");
								Row dataRow = researchSheet.createRow(rowCount++);
								dataRow.createCell(0).setCellValue(rowCount - 2);
								dataRow.createCell(1).setCellValue(teacher.getName());
								dataRow.createCell(2).setCellValue(teacher.getEmail());
								dataRow.createCell(3).setCellValue(file.getTitle());
								dataRow.createCell(4).setCellValue(file.getPublicationName());
								dataRow.createCell(5).setCellValue(file.getPublicationType());
								dataRow.createCell(6).setCellValue(file.getDate().toString()); // Assuming date is a
																								// LocalDate
								dataRow.createCell(7).setCellValue(file.getISSN());
								dataRow.createCell(8).setCellValue(file.getDOI());
								dataRow.createCell(9).setCellValue(file.getVolume());
							}
						}
					}
					break;
				case "BOOK OR CHAPTER":
					// Create header row
					headerRow = booksSheet.createRow(1);
					headerRow.createCell(0).setCellValue("Sr. No");
					headerRow.createCell(1).setCellValue("Name of Faculty");
					headerRow.createCell(2).setCellValue("Email");
					headerRow.createCell(3).setCellValue("Title");
					headerRow.createCell(4).setCellValue("Publication Name");
					headerRow.createCell(5).setCellValue("Book/Chapter");
					headerRow.createCell(6).setCellValue("Date of Publishing");
					headerRow.createCell(7).setCellValue("ISBN Number");

					// Apply header cell style
					headerRow.forEach(cell -> cell.setCellStyle(headerStyle));

					rowCount = 2;

					for (UserDtls teacher : teachers) {
						for (DatabaseFile file : teacher.getFiles()) {
							if (file.getType() == FileType.BOOK_OR_CHAPTER) {
								System.out.println("\u100B" + file.getType() + "\u100B");
								Row dataRow = booksSheet.createRow(rowCount++);
								dataRow.createCell(0).setCellValue(rowCount - 2);
								dataRow.createCell(1).setCellValue(teacher.getName());
								dataRow.createCell(2).setCellValue(teacher.getEmail());
								dataRow.createCell(3).setCellValue(file.getTitle());
								dataRow.createCell(4).setCellValue(file.getPublicationName());
								dataRow.createCell(5).setCellValue(file.getPublicationType());
								dataRow.createCell(6).setCellValue(file.getDate().toString()); // Assuming date is a
																								// LocalDate
								dataRow.createCell(7).setCellValue(file.getISBN());
							}
						}
					}
					break;
				case "FDP":
					// Create header row
					headerRow = fdpSheet.createRow(1);
					headerRow.createCell(0).setCellValue("Sr. No");
					headerRow.createCell(1).setCellValue("Name of Faculty");
					headerRow.createCell(2).setCellValue("Email");
					headerRow.createCell(3).setCellValue("Title");
					headerRow.createCell(4).setCellValue("Online/Offline");
					headerRow.createCell(5).setCellValue("Number of days");
					headerRow.createCell(6).setCellValue("Organization");

					// Apply header cell style
					headerRow.forEach(cell -> cell.setCellStyle(headerStyle));

					rowCount = 2;

					for (UserDtls teacher : teachers) {
						for (DatabaseFile file : teacher.getFiles()) {
							if (file.getType() == FileType.FDP) {
								System.out.println("\u100B" + file.getType() + "\u100B");
								Row dataRow = fdpSheet.createRow(rowCount++);
								dataRow.createCell(0).setCellValue(rowCount - 2);
								dataRow.createCell(1).setCellValue(teacher.getName());
								dataRow.createCell(2).setCellValue(teacher.getEmail());
								dataRow.createCell(3).setCellValue(file.getTitle());
								dataRow.createCell(4).setCellValue(file.getNature());
								dataRow.createCell(5).setCellValue(file.getNoOfDays());
								dataRow.createCell(6).setCellValue(file.getOrganizedBy());
							}
						}
					}
					break;

				case "STTP":
					// Create header row
					headerRow = sttpSheet.createRow(1);
					headerRow.createCell(0).setCellValue("Sr. No");
					headerRow.createCell(1).setCellValue("Name of Faculty");
					headerRow.createCell(2).setCellValue("Email");
					headerRow.createCell(3).setCellValue("Title");
					headerRow.createCell(4).setCellValue("Online/Offline");
					headerRow.createCell(5).setCellValue("Number of days");
					headerRow.createCell(6).setCellValue("Organization");

					// Apply header cell style
					headerRow.forEach(cell -> cell.setCellStyle(headerStyle));

					rowCount = 2;

					for (UserDtls teacher : teachers) {
						for (DatabaseFile file : teacher.getFiles()) {
							if (file.getType() == FileType.STTP) {
								System.out.println("\u100B" + file.getType() + "\u100B");
								Row dataRow = sttpSheet.createRow(rowCount++);
								dataRow.createCell(0).setCellValue(rowCount - 2);
								dataRow.createCell(1).setCellValue(teacher.getName());
								dataRow.createCell(2).setCellValue(teacher.getEmail());
								dataRow.createCell(3).setCellValue(file.getTitle());
								dataRow.createCell(4).setCellValue(file.getNature());
								dataRow.createCell(5).setCellValue(file.getNoOfDays());
								dataRow.createCell(6).setCellValue(file.getOrganizedBy());
							}
						}
					}
					break;

				case "QIP":
					// Create header row
					headerRow = qipSheet.createRow(1);
					headerRow.createCell(0).setCellValue("Sr. No");
					headerRow.createCell(1).setCellValue("Name of Faculty");
					headerRow.createCell(2).setCellValue("Email");
					headerRow.createCell(3).setCellValue("Title");
					headerRow.createCell(4).setCellValue("Online/Offline");
					headerRow.createCell(5).setCellValue("Number of days");
					headerRow.createCell(6).setCellValue("Organization");

					// Apply header cell style
					headerRow.forEach(cell -> cell.setCellStyle(headerStyle));

					rowCount = 2;

					for (UserDtls teacher : teachers) {
						for (DatabaseFile file : teacher.getFiles()) {
							if (file.getType() == FileType.QIP) {
								System.out.println("\u100B" + file.getType() + "\u100B");
								Row dataRow = qipSheet.createRow(rowCount++);
								dataRow.createCell(0).setCellValue(rowCount - 2);
								dataRow.createCell(1).setCellValue(teacher.getName());
								dataRow.createCell(2).setCellValue(teacher.getEmail());
								dataRow.createCell(3).setCellValue(file.getTitle());
								dataRow.createCell(4).setCellValue(file.getNature());
								dataRow.createCell(5).setCellValue(file.getNoOfDays());
								dataRow.createCell(6).setCellValue(file.getOrganizedBy());
							}
						}
					}
					break;
				case "CONFERENCE WORKSHOP SEMINAR":
					// Create header row
					headerRow = conferenceSheet.createRow(1);
					headerRow.createCell(0).setCellValue("Sr. No");
					headerRow.createCell(1).setCellValue("Name of Faculty");
					headerRow.createCell(2).setCellValue("Email");
					headerRow.createCell(3).setCellValue("Title");
					headerRow.createCell(4).setCellValue("Type");
					headerRow.createCell(5).setCellValue("Organiser/Attendee");
					headerRow.createCell(6).setCellValue("Online/Offline");
					headerRow.createCell(7).setCellValue("Number of days");
					headerRow.createCell(8).setCellValue("Organization");

					// Apply header cell style
					headerRow.forEach(cell -> cell.setCellStyle(headerStyle));

					rowCount = 2;

					for (UserDtls teacher : teachers) {
						for (DatabaseFile file : teacher.getFiles()) {
							if (file.getType() == FileType.CONFERENCE_WORKSHOP_SEMINAR) {
								System.out.println("\u100B" + file.getType() + "\u100B");
								Row dataRow = conferenceSheet.createRow(rowCount++);
								dataRow.createCell(0).setCellValue(rowCount - 2);
								dataRow.createCell(1).setCellValue(teacher.getName());
								dataRow.createCell(2).setCellValue(teacher.getEmail());
								dataRow.createCell(3).setCellValue(file.getTitle());
								dataRow.createCell(4).setCellValue(file.getEventType());
								dataRow.createCell(5).setCellValue(file.getOrganizedBy());
								dataRow.createCell(6).setCellValue(file.getNature());
								dataRow.createCell(7).setCellValue(file.getNoOfDays());
								dataRow.createCell(8).setCellValue(file.getOrganizedBy());
							}
						}
					}
					break;
				case "INDUSTRIAL VISIT":
					// Create header row
					headerRow = industrialVisitsSheet.createRow(1);
					headerRow.createCell(0).setCellValue("Sr. No");
					headerRow.createCell(1).setCellValue("Name of Faculty");
					headerRow.createCell(2).setCellValue("Email");
					headerRow.createCell(3).setCellValue("Name of industry visited & Place");
					headerRow.createCell(4).setCellValue("No. of students visited");
					headerRow.createCell(5).setCellValue("Date of visit");

					// Apply header cell style
					headerRow.forEach(cell -> cell.setCellStyle(headerStyle));

					rowCount = 2;

					for (UserDtls teacher : teachers) {
						for (DatabaseFile file : teacher.getFiles()) {
							if (file.getType() == FileType.INDUSTRIALVISIT) {
								System.out.println("\u100B" + file.getType() + "\u100B");
								Row dataRow = industrialVisitsSheet.createRow(rowCount++);
								dataRow.createCell(0).setCellValue(rowCount - 2);
								dataRow.createCell(1).setCellValue(teacher.getName());
								dataRow.createCell(2).setCellValue(teacher.getEmail());
								dataRow.createCell(3).setCellValue(file.getTitle());
								dataRow.createCell(4).setCellValue(file.getNoOfStudentsVisited());
								dataRow.createCell(5).setCellValue(file.getDate());
							}
						}
					}
					break;
				case "GUEST LECTURE":
					// Create header row
					headerRow = guestLectureSheet.createRow(1);
					headerRow.createCell(0).setCellValue("Sr. No");
					headerRow.createCell(1).setCellValue("Name of Faculty");
					headerRow.createCell(2).setCellValue("Email");
					headerRow.createCell(3).setCellValue("Topic of lecture");
					headerRow.createCell(4).setCellValue("Online/Offline");
					headerRow.createCell(5).setCellValue("Date");
					headerRow.createCell(6).setCellValue("Place/Event where the lecture was delivered");

					// Apply header cell style
					headerRow.forEach(cell -> cell.setCellStyle(headerStyle));

					rowCount = 2;

					for (UserDtls teacher : teachers) {
						for (DatabaseFile file : teacher.getFiles()) {
							if (file.getType() == FileType.GUESTLECTURE) {
								System.out.println("\u100B" + file.getType() + "\u100B");
								Row dataRow = guestLectureSheet.createRow(rowCount++);
								dataRow.createCell(0).setCellValue(rowCount - 2);
								dataRow.createCell(1).setCellValue(teacher.getName());
								dataRow.createCell(2).setCellValue(teacher.getEmail());
								dataRow.createCell(3).setCellValue(file.getTitle());
								dataRow.createCell(4).setCellValue(file.getNature());
								dataRow.createCell(5).setCellValue(file.getDate());
								dataRow.createCell(6).setCellValue(file.getOrganizedBy());
							}
						}
					}
					break;
				case "PATENT":
					// Create header row
					headerRow = patentSheet.createRow(1);
					headerRow.createCell(0).setCellValue("Sr. No");
					headerRow.createCell(1).setCellValue("Name of Faculty");
					headerRow.createCell(2).setCellValue("Email");
					headerRow.createCell(3).setCellValue("Title");
					headerRow.createCell(4).setCellValue("Patent Number");
					headerRow.createCell(5).setCellValue("Date of Patent");
					headerRow.createCell(6).setCellValue("Status");

					// Apply header cell style
					headerRow.forEach(cell -> cell.setCellStyle(headerStyle));

					rowCount = 2;

					for (UserDtls teacher : teachers) {
						for (DatabaseFile file : teacher.getFiles()) {
							if (file.getType() == FileType.PATENT) {
								System.out.println("\u100B" + file.getType() + "\u100B");
								Row dataRow = patentSheet.createRow(rowCount++);
								dataRow.createCell(0).setCellValue(rowCount - 2);
								dataRow.createCell(1).setCellValue(teacher.getName());
								dataRow.createCell(2).setCellValue(teacher.getEmail());
								dataRow.createCell(3).setCellValue(file.getTitle());
								dataRow.createCell(4).setCellValue(file.getPatentNumber());
								dataRow.createCell(5).setCellValue(file.getDate());
								dataRow.createCell(6).setCellValue(file.getPatentStatus());
							}
						}
					}
					break;

			}

		}
		// Set up the response to return an Excel file
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"dept-details-" + java.time.LocalDate.now() + ".xlsx\"");

		// Write the Excel workbook to the response
		workbook.write(response.getOutputStream());
		workbook.close();
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
		Optional<UserDtls> optionalTeacher = userRepo.findById(teacherId);
		if (optionalTeacher.isPresent()) {
			UserDtls teacher = optionalTeacher.get();

			// Check if the role is either ROLE_TEACHER or ROLE_HOD so that the admin cannot
			// change the role of the admin or any other non-existing role for that matter
			Set<String> allowedRoles = new HashSet<>(Arrays.asList("ROLE_TEACHER", "ROLE_HOD"));
			if (allowedRoles.contains(role)) {
				// Check if the selected role is HOD
				if (role.equals("ROLE_HOD")) {
					// If the selected role is HOD, check if there's already an HOD in the
					// department
					List<UserDtls> hodList = userRepo.findByBranchAndRole(teacher.getBranch(), "ROLE_HOD");
					if (!hodList.isEmpty()) {
						// If there's already an HOD, change the first one to teacher
						UserDtls currentHOD = hodList.get(0);
						currentHOD.setRole("ROLE_TEACHER");
						userRepo.save(currentHOD);
					}
				}
				// Update the role for the teacher
				teacher.setRole(role);
				// Save the updated teacher to the database
				userRepo.save(teacher);
			}
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

			} else {

			}

		} else {

		}

		return "redirect:/admin/settings";

	}

}
