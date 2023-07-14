package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.DatabaseFile;
import com.example.demo.model.UserDtls;

@Repository
public interface DatabaseFileRepository extends JpaRepository<DatabaseFile, String> {
    List<DatabaseFile> findByUser(UserDtls user);

    List<DatabaseFile> findByUserAndType(UserDtls user, DatabaseFile.FileType type);

    List<DatabaseFile> findByType(DatabaseFile.FileType type);
}
