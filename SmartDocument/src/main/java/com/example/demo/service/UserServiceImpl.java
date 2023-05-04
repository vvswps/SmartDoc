package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.PersonalDtls;
import com.example.demo.model.UserDtls;
import com.example.demo.repositary.UserRepositary;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepositary userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncode;

	@Override
	public UserDtls createUser(UserDtls user, String role) {
		user.setPassword(passwordEncode.encode(user.getPassword()));
		user.setRole(role);

		PersonalDtls personalDtls = new PersonalDtls();
		personalDtls.setId(user.getId());
		personalDtls.setUser(user);
		user.setPersonalDtls(personalDtls);

		return userRepo.save(user);
	}

	@Override
	public boolean checkEmail(String email) {

		return userRepo.existsByEmail(email);
	}

}
