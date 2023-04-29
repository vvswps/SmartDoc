package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.PersonalDtls;
import com.example.demo.repositary.personalRepository;

@Service
public class PersonalServiceImpl implements PersonalService {

	@Autowired
	private personalRepository personalRepo;

	@Override
	public PersonalDtls personalUser(PersonalDtls puser) {

		return personalRepo.save(puser);
	}

}
