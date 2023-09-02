/*
 * Handles the file upload requests
 */

package com.example.demo.controller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.model.DatabaseFile;
import com.example.demo.model.UserDtls;
import com.example.demo.model.DatabaseFile.FileType;
import com.example.demo.repository.DatabaseFileRepository;
import com.example.demo.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class FileUploadController {

    @Autowired
    private DatabaseFileRepository DatabaseFileRepository;

    @Autowired
    private UserRepository UserRepository;

    @PostMapping("/uploadProfilePicture")
    public ResponseEntity<?> uploadProfilePicture(@RequestParam MultipartFile file, Principal principal,
            HttpServletRequest request) throws IOException {
        System.out.println("\n\n\n\nuploadProfilePicture\n\n\n\n");

        // Check if the user is authenticated
        if (principal == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }

        try {
            // Get the user details based on the authenticated principal
            UserDtls user = UserRepository.findByEmail(principal.getName());

            // Find the profile picture of the user, if it exists
            List<DatabaseFile> profileList = DatabaseFileRepository.findByUserAndType(user, FileType.PROFILE_PICTURE);
            DatabaseFile document;

            // Update existing profile picture or create a new one
            if (!profileList.isEmpty()) {
                document = profileList.get(0);
                document.setProfilePicture(file.getBytes());
                document.setFileName(file.getOriginalFilename());
                document.setFileType(file.getContentType());
                document.setData(file.getBytes());
            } else {
                document = new DatabaseFile();
                document.setProfilePicture(file.getBytes());
                document.setType(FileType.PROFILE_PICTURE);
                document.setFileName(file.getOriginalFilename());
                document.setFileType(file.getContentType());
                document.setData(file.getBytes());
                document.setUserId(user);
            }

            // Save the updated or new profile picture
            DatabaseFileRepository.save(document);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Redirect back to the referring page after the upload
        String referer = request.getHeader("Referer");
        System.out.println("\u001B[32m" + "\nRedirecting to: " + referer + "\n" + "\u001B[0m");
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(referer));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
        // return new ResponseEntity<>("Profile picture uploaded successfully",
        // HttpStatus.OK);
    }

    @PostMapping("/uploadDoc")
    public ResponseEntity<?> uploadDoc(@RequestParam MultipartFile file,
            @RequestParam("type") String fileType,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String publicationType,
            @RequestParam(required = false) String publicationName,
            @RequestParam(required = false) String ISBN,
            @RequestParam(required = false) String ISSN,
            @RequestParam(required = false) String DOI,
            @RequestParam(required = false) String volume,
            @RequestParam(required = false) String awardingInstitution,
            @RequestParam(required = false) String nature,
            @RequestParam(required = false) String durationFrom,
            @RequestParam(required = false) String durationTo,
            @RequestParam(required = false) Integer noOfDays,
            @RequestParam(required = false) String organizedBy,
            @RequestParam(required = false) String eventRole,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String patentStatus,
            @RequestParam(required = false) String patentNumber,
            @RequestParam(required = false) String lectureTopic,
            @RequestParam(required = false) String placeOrEvent,
            @RequestParam(required = false) String noOfStudentsVisited,
            Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }

        try {
            DatabaseFile document = new DatabaseFile();
            document.setFileName(file.getOriginalFilename());
            document.setFileType(file.getContentType());
            document.setData(file.getBytes());

            UserDtls user = UserRepository.findByEmail(principal.getName());
            document.setUserId(user);

            document.setTitle(title);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(date);

            // if file type is research, book, award, achievement then setDate otherwise
            // setDurationFrom and setDurationTo
            if (new HashSet<String>(Arrays.asList("research", "book", "award", "achievement", "patent", "guestlecture",
                    "industrialvisit")).contains(fileType.toLowerCase())) {
                document.setDate(parsedDate);
            } else {
                System.out.println("\n\n\n File Type: " + fileType + "\n\n\n");
                document.setNature(nature);
                document.setDurationFrom(durationFrom);
                document.setDurationTo(durationTo);

                if (noOfDays < 0)
                    noOfDays = 0;
                document.setNoOfDays(noOfDays);

                document.setOrganizedBy(organizedBy);
            }

            // Set file type and additional fields based on the given type
            switch (fileType.toLowerCase()) {
                case "research":
                    document.setType(DatabaseFile.FileType.RESEARCH_PAPER);
                    document.setPublicationName(publicationName);
                    document.setPublicationType(publicationType);
                    document.setISSN(ISSN);
                    document.setDOI(DOI);
                    document.setVolume(volume);
                    DatabaseFileRepository.save(document);

                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("teacher/research")).build();

                case "book":
                    document.setType(DatabaseFile.FileType.BOOK_OR_CHAPTER);
                    document.setPublicationName(publicationName);
                    document.setPublicationType(publicationType);
                    document.setISBN(ISBN);
                    DatabaseFileRepository.save(document);

                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("teacher/research")).build();

                case "award":
                    document.setType(DatabaseFile.FileType.AWARD);
                    document.setAwardingInstitution(awardingInstitution);
                    DatabaseFileRepository.save(document);

                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("teacher/awards")).build();

                case "patent":
                    document.setType(DatabaseFile.FileType.PATENT);
                    document.setPatentStatus(patentStatus);
                    document.setPatentNumber(patentNumber);

                    DatabaseFileRepository.save(document);

                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("teacher/awards")).build();

                case "fdp":
                    document.setType(DatabaseFile.FileType.FDP);
                    DatabaseFileRepository.save(document);

                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("teacher/fdp")).build();

                case "sttp":
                    document.setType(DatabaseFile.FileType.STTP);
                    DatabaseFileRepository.save(document);

                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("teacher/fdp")).build();

                case "qip":
                    document.setType(DatabaseFile.FileType.QIP);
                    DatabaseFileRepository.save(document);

                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("teacher/fdp")).build();

                case "conference_workshop_seminar":
                    document.setEventType(eventType);
                    document.setType(DatabaseFile.FileType.CONFERENCE_WORKSHOP_SEMINAR);
                    DatabaseFileRepository.save(document);

                    return ResponseEntity.status(HttpStatus.FOUND)
                            .location(URI.create("teacher/conferenceWorkshopSeminar")).build();

                case "guestlecture":
                    document.setType(DatabaseFile.FileType.GUESTLECTURE);
                    document.setLectureTopic(lectureTopic);
                    document.setPlaceOrEvent(placeOrEvent);
                    document.setNature(nature);
                    DatabaseFileRepository.save(document);

                    return ResponseEntity.status(HttpStatus.FOUND)
                            .location(URI.create("teacher/guestLectAndIndustrialVisits")).build();

                case "industrialvisit":
                    document.setType(DatabaseFile.FileType.INDUSTRIALVISIT);
                    document.setPlaceOrEvent(placeOrEvent);
                    document.setNoOfStudentsVisited(noOfStudentsVisited);
                    DatabaseFileRepository.save(document);

                    return ResponseEntity.status(HttpStatus.FOUND)
                            .location(URI.create("teacher/guestLectAndIndustrialVisits"))
                            .build();
                default:
                    // Handle invalid file types
                    return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("<h1>Something went wrong</h1>");
        }
    }
}