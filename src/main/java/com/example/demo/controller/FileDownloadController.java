/*
 * Handles file download requests
 */

package com.example.demo.controller;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.DatabaseFile;
import com.example.demo.model.UserDtls;
import com.example.demo.model.DatabaseFile.FileType;
import com.example.demo.repository.DatabaseFileRepository;
import com.example.demo.repository.UserRepository;

@RestController
public class FileDownloadController {

    @Autowired
    private DatabaseFileRepository DatabaseFileRepository;

    @Autowired
    private UserRepository UserRepository;

    @GetMapping("/documents/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {
        try {
            Optional<DatabaseFile> optionalDocument = DatabaseFileRepository.findById(id);
            if (optionalDocument.isPresent()) {
                DatabaseFile document = optionalDocument.get();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentDisposition(
                        ContentDisposition.builder("attachment").filename(document.getFileName()).build());
                headers.setContentType(MediaType.parseMediaType(document.getFileType()));
                return new ResponseEntity<>(document.getData(), headers, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            String errorMessage = "Failed to download the file.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorMessage.getBytes(StandardCharsets.UTF_8));
        }
    }

    @GetMapping("/profilePicture")
    public ResponseEntity<byte[]> downloadProfilePicture(Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserDtls user = UserRepository.findByEmail(principal.getName());
        List<DatabaseFile> profileList = DatabaseFileRepository.findByUserAndType(user, FileType.PROFILE_PICTURE);

        if (!profileList.isEmpty()) {
            DatabaseFile file = profileList.get(0);
            HttpHeaders headers = new HttpHeaders();

            headers.setContentDisposition(
                    ContentDisposition.builder("attachment").filename(file.getFileName()).build());
            headers.setContentType(MediaType.parseMediaType(file.getFileType()));
            return new ResponseEntity<>(file.getData(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}