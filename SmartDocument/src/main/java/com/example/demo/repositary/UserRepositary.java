package com.example.demo.repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.UserDtls;

public interface UserRepositary extends JpaRepository<UserDtls,Integer> {
	
	public boolean existsByEmail(String email);
	
	public UserDtls findByEmail(String email);
	
	public UserDtls findByEmailAndMobileNumber(String em, String mobno);

}
