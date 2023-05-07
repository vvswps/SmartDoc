package com.example.demo.controller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.model.DatabaseFile;
import com.example.demo.model.UserDtls;
import com.example.demo.repositary.DatabaseFileRepository;
import com.example.demo.repositary.UserRepositary;

@RestController
public class FileUploadController {

    @Autowired
    private DatabaseFileRepository DatabaseFileRepository;

    @Autowired
    private UserRepositary UserRepositary;

    @PostMapping("/uploadDoc")
    public ResponseEntity<?> uploadDoc(@RequestParam("file") MultipartFile file,
            @RequestParam("type") String fileType,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "publicationType", required = false) String publicationType,
            @RequestParam(value = "publicationName", required = false) String publicationName,
            @RequestParam(value = "ISBN", required = false) String ISBN,
            @RequestParam(value = "ISSN", required = false) String ISSN,
            @RequestParam(value = "DOI", required = false) String DOI,
            @RequestParam(value = "volume", required = false) String volume,
            @RequestParam(value = "awardingInstitution", required = false) String awardingInstitution,
            @RequestParam(value = "nature", required = false) String nature,
            @RequestParam(value = "durationFrom", required = false) String durationFrom,
            @RequestParam(value = "durationTo", required = false) String durationTo,
            @RequestParam(value = "noOfDays", required = false) Integer noOfDays,
            @RequestParam(value = "organizedBy", required = false) String organizedBy,
            Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }

        try {
            DatabaseFile document = new DatabaseFile();
            document.setFileName(file.getOriginalFilename());
            document.setFileType(file.getContentType());
            document.setData(file.getBytes());

            UserDtls user = UserRepositary.findByEmail(principal.getName());
            document.setUserId(user);

            document.setTitle(title);

            // if file type is research, book, award, achievement then setDate othwerwise
            // setDurationFrom and setDurationTo
            if (fileType.toLowerCase().equals("research") || fileType.toLowerCase().equals("book")
                    || fileType.toLowerCase().equals("award") || fileType.toLowerCase().equals("achievement")) {
                document.setDate(date);
            } else {
                document.setNature(nature);
                document.setDurationFrom(durationFrom);
                document.setDurationTo(durationTo);
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
                    document.setISBN(ISBN);
                    DatabaseFileRepository.save(document);

                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("teacher/research")).build();

                case "award":
                    document.setType(DatabaseFile.FileType.AWARD);
                    document.setAwardingInstitution(awardingInstitution);
                    DatabaseFileRepository.save(document);

                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("teacher/awards")).build();

                case "achievement":
                    document.setType(DatabaseFile.FileType.ACHIEVEMENT);
                    document.setAwardingInstitution(awardingInstitution);
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

                case "workshop":
                    document.setType(DatabaseFile.FileType.WORKSHOP);
                    DatabaseFileRepository.save(document);

                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("teacher/fdp")).build();

                default:
                    // Handle invalid file types
                    return ResponseEntity.badRequest().build();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // use this method to upload a file to the file system

    // @PostMapping("/uploadafile")
    // public ResponseEntity<String> uploadaFile(@RequestParam("file") MultipartFile
    // file) {

    // try {
    // if (file.isEmpty()) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Must
    // upload a file");
    // }

    // boolean uploaded_sucessfully = fileUploadHelper.uploadFile(file);

    // if (uploaded_sucessfully) {
    // return ResponseEntity.ok("Successfully uploaded");
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return
    // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went
    // wrong");
    // }

}