package com.example.demo.repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.DatabaseFile;


public interface DatabaseFileRepository extends JpaRepository<DatabaseFile,String> {
	
	

}
