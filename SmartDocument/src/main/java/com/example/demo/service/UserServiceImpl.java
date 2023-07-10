package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.PersonalDtls;
import com.example.demo.model.UserDtls;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.personalRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private personalRepository personalRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncode;

	@Override
	public UserDtls createUser(UserDtls user, String role) {
		user.setPassword(passwordEncode.encode(user.getPassword()));
		user.setRole(role);
		UserDtls newUser = userRepo.save(user);
		PersonalDtls personalDtls = new PersonalDtls();
		// System.out.println("User ID: " + newUser.getId());
		// personalDtls.setId(19);

		personalDtls.setUser(user); // set the user to the personal details object
		user.setPersonalDtls(personalDtls);

		personalDtls = personalRepository.save(personalDtls); // save the personal details object to get the ID

		return newUser;
	}
	

	
	

	
	
	
	@Override
	public boolean checkEmail(String email) {

		return userRepo.existsByEmail(email);
	}
	
//	 @Override
//	    public List<UserDtls> getAllUsers() {
//	        return userRepo.findAll();
//	    }
//
//	 @Override
//	    public UserDtls getUserById(int id) {
//	        return userRepo.findById(id).orElse(null);
//	    }
//
//	 @Override
//	    public UserDtls saveUser(UserDtls user) {
//	        return userRepo.save(user);
//	    }

}
