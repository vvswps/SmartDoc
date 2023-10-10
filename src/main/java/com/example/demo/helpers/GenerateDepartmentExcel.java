package com.example.demo.helpers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.stereotype.Component;

import com.example.demo.model.DatabaseFile;
import com.example.demo.model.DatabaseFile.FileType;
import com.example.demo.model.UserDtls;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class GenerateDepartmentExcel {
    Logger logger;

    public void downloadDeptExcel(List<String> selectedFileTypes, HttpServletResponse response, String deptName,
            List<UserDtls> teachers) throws IOException {

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

}
