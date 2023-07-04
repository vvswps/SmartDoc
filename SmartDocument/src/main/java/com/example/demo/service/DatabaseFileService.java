/*
 * This is a service class that handles the storage and retrieval of files in a database.
 * It provides methods to store a file and retrieve a file by its ID.
 * 
 * Service classes are used to implement business logic in a separate layer from the controller.
 */

package com.example.demo.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.FileNotFoundException;
import com.example.demo.exception.FileStorageException;
import com.example.demo.model.DatabaseFile;
import com.example.demo.repository.DatabaseFileRepository;

@Service
public class DatabaseFileService {

    @Autowired
    private DatabaseFileRepository dbFileRepository;

    /**
     * Stores a file in the database.
     *
     * @param file The file to be stored.
     * @return The saved DatabaseFile object.
     * @throws FileStorageException If an error occurs while storing the file.
     */
    public DatabaseFile storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Create a new DatabaseFile object with the file details
            DatabaseFile dbFile = new DatabaseFile(fileName, file.getContentType(), file.getBytes());

            // Save the file in the database
            return dbFileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * Retrieves a file from the database by its ID.
     *
     * @param fileId The ID of the file to be retrieved.
     * @return The DatabaseFile object representing the retrieved file.
     * @throws FileNotFoundException If the file with the specified ID is not found.
     */
    public DatabaseFile getFile(String fileId) {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
    }
}
