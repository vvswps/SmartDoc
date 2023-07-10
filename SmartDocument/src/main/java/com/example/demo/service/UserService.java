package com.example.demo.service;

import java.util.List;

import org.springframework.security.core.userdetails.User;

import com.example.demo.model.UserDtls;

public interface UserService {

	public UserDtls createUser(UserDtls user, String role);

	public boolean checkEmail(String email);
	
//	List<UserDtls> getAllUsers();
//
//	UserDtls getUserById(int id);
//
//    UserDtls saveUser(UserDtls user);

	

	

	



	

	



}
