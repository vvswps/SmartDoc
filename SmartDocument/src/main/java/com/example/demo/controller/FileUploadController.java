package com.example.demo.controller;

import java.io.IOException;
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
    public ResponseEntity<?> uploadDoc(@RequestParam("file") MultipartFile file, Principal principal) {
        if (principal == null) {

            // Handle case where user is not authenticated
            // You can redirect to a login page or display an error message
            // return "redirect:/login";
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }

        try {
            DatabaseFile document = new DatabaseFile();
            document.setFileName(file.getOriginalFilename());
            document.setFileType(file.getContentType());
            document.setData(file.getBytes());

            UserDtls user = UserRepositary.findByEmail(principal.getName());

            document.setUserId(user);

            DatabaseFileRepository.save(document);
            return ResponseEntity.ok().build();
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