package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.model.PersonalDtls;
import com.example.demo.repositary.personalRepository;

public class PersonalServiceImpl implements PersonalService {
	
	@Autowired
	private personalRepository personalRepo;

	@Override
	public PersonalDtls personalUser(PersonalDtls user) {
		
		return personalRepo.save(user);
	}
	

}
