package com.example.demo.service;

import org.springframework.ui.Model;

import com.example.demo.model.UserDtls;

public interface UserService {

	public UserDtls createUser(UserDtls user, String role, Model model);

	public boolean checkEmail(String email);

}
