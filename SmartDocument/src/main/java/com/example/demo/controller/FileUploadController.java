package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.helper.FileUploadHelper;
import com.example.demo.model.DatabaseFile;
import com.example.demo.payload.Response;

import com.example.demo.service.DatabaseFileService;





@RestController
public class FileUploadController {

    @Autowired
    private DatabaseFileService fileStorageService;
    
    @Autowired
    private FileUploadHelper fileUploadHelper;
    
    @PostMapping("/uploadFile")
    public Response uploadFile(@RequestParam("file") MultipartFile file) {
    	System.out.println("Upload method run");
    	DatabaseFile fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName.getFileName())
                .toUriString();

        return new Response(fileName.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }
    
    @PostMapping("/upload")
    public Response uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("title") String title, Principal principal) throws IOException {
    	/*
    	 * if (principal == null) {
    	 
            // Handle case where user is not authenticated
            // You can redirect to a login page or display an error message
            return "redirect:/login";
        }
        */
    	//        String userEmail = principal.getName(); // Get the email of the authenticated user
    	
    	String userEmail = "test@mail.com";
        String originalFilename = file.getOriginalFilename();
//        String extension = FilenameUtils.getExtension(originalFilename);
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFileName = userEmail + "_" + title + extension; // Construct the unique file name
        String uploadDir = "uploads"; // The directory where the file will be uploaded
        String filePath = Paths.get(uploadDir, uniqueFileName).toString(); // The full path of the uploaded file
        System.out.println("Path = " + filePath);

        // Save the file to disk with the unique name
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filePath);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Response(originalFilename, uniqueFileName,
                file.getContentType(), file.getSize());
    }
    
    @PostMapping("/uploadafile")
    public ResponseEntity<String> uploadaFile(@RequestParam("file") MultipartFile file){
    	
    	try {
    	if(file.isEmpty()) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Must upload a file");
    	}

    	boolean uploaded_sucessfully = fileUploadHelper.uploadFile(file);

    	if(uploaded_sucessfully) {
    		return ResponseEntity.ok("Successfully uploaded");    	
    	}
    	}catch (Exception e) {
			e.printStackTrace();
		}    	
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
    }

    @PostMapping("/uploadMultipleFiles")
    public List<Response> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
    	System.out.println("Multiple upload method run");
    	return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }
}