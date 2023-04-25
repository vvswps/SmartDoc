package com.example.demo.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadHelper {
	public final String UPLOAD_DIR = "C:\\Users\\as904\\git\\SmartDoc\\SmartDocument\\src\\main\\resources\\static\\files";
	
	public boolean uploadFile(MultipartFile file) {
		boolean uploaded_successfully = false;
		
		try {
			
			Files.copy(file.getInputStream(), Paths.get(UPLOAD_DIR + File.separator+ file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
			
			uploaded_successfully = true;
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return uploaded_successfully;
	}

}
