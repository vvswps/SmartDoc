package com.example.demo.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.DatabaseFile;
import com.example.demo.model.UserDtls;
import com.example.demo.repositary.DatabaseFileRepository;
import com.example.demo.repositary.UserRepositary;

@RestController
public class FileController {
	@Autowired
	private UserRepositary userRepo;

	@Autowired
	private DatabaseFileRepository fileRepo;

	@ModelAttribute
	// private void documentDetails(Model model, Principal p) {
	// Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);
	// logger.info("\n\n\nDoc details are working fine\n\n\n");

	// String email = p.getName();
	// UserDtls user = userRepo.findByEmail(email);

	// // log user details
	// System.out.println("User details:");
	// System.out.println("Name: " + user.getName());
	// System.out.println("Email: " + user.getEmail());

	// Optional<DatabaseFile> files =
	// fileRepo.findById(Integer.toString(user.getId()));
	// List<DatabaseFile> fileList =
	// files.map(Collections::singletonList).orElse(Collections.emptyList());
	// model.addAttribute("files", fileList);

	// System.out.println("Files: " + files);
	// // log files info
	// for (DatabaseFile file : fileList) {
	// logger.info(" File: " + file.getFileName());
	// }
	// }

	@PostMapping("/upload-file")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty())
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No file uploaded");
		// if(file.getContentType().equals("image/jpeg")) return
		// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Only images
		// allowed");

		return ResponseEntity.ok("working");
	}
}
