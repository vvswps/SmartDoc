package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.PersonalDtls;
import com.example.demo.repository.personalRepository;

@Service
public class PersonalServiceImpl implements PersonalService {

	@Autowired
	private personalRepository personalRepo;

	@Override
	public PersonalDtls savePersonalDtls(PersonalDtls puser) {
		return personalRepo.save(puser);
	}
}
