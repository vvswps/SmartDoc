package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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

	@Autowired
	private PasswordValidatorService passwordValidator;

	@Override
	public UserDtls createUser(UserDtls user, String role, Model model) {
		String validationMessage = passwordValidator.validatePasswordAndEmail(user.getPassword(),
				user.getEmail());
		if (validationMessage != null) {
			model.addAttribute("regMsg", validationMessage);
		}

		user.setPassword(passwordEncode.encode(user.getPassword()));
		user.setRole(role);

		user.setEmail(user.getEmail().toLowerCase());
		UserDtls newUser = userRepo.save(user);
		PersonalDtls personalDtls = new PersonalDtls();

		// personalDtls.setId(19);

		personalDtls.setUser(user); // set the user to the personal details object
		personalDtls.setName(user.getName());
		personalDtls.setOffEmail(user.getEmail());
		personalDtls.setDept(user.getBranch());
		personalDtls.setMobileNumber(user.getMobileNumber());

		user.setPersonalDtls(personalDtls);

		personalDtls = personalRepository.save(personalDtls); // save the personal details object to get the ID

		return newUser;
	}

	@Override
	public boolean checkEmail(String email) {

		return userRepo.existsByEmail(email);
	}
}
