package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class PersonalDtls {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserDtls user;

	private String name;
	private String erpId;
	private String offEmail;
	private String perEmail;
	private String dept;
	private String whatsNumber;
	private String mobileNumber;
	private String gender;
	private String dob;
	private String expInd;
	private String expAcd;
	private String doj;
	private String dol;
	private String googleId;
	private String scopusId;
	private String sciId;
	private String curAdd;
	private String currCity;
	private String currState;
	private String currCunt;
	private String currPin;
	private String perAdd;
	private String perCity;
	private String perState;
	private String perCunt;
	private String perPin;
}
