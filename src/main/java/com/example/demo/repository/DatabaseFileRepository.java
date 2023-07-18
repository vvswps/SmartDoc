package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.DatabaseFile;
import com.example.demo.model.UserDtls;

public interface DatabaseFileRepository extends JpaRepository<DatabaseFile, String> {
    List<DatabaseFile> findByUser(UserDtls user);

    List<DatabaseFile> findByUserAndType(UserDtls user, DatabaseFile.FileType type);

    List<DatabaseFile> findByType(DatabaseFile.FileType type);

    Optional<DatabaseFile> findById(String id);
}
