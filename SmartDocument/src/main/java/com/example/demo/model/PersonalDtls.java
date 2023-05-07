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

	public UserDtls getUser() {
		return user;
	}

	public void setUser(UserDtls user) {
		this.user = user;
		// this.id = user.getId();
	}

	public String getCurrCity() {
		return currCity;
	}

	public void setCurrCity(String currCity) {
		this.currCity = currCity;
	}

	public String getCurrState() {
		return currState;
	}

	public void setCurrState(String currState) {
		this.currState = currState;
	}

	public String getCurrCunt() {
		return currCunt;
	}

	public void setCurrCunt(String currCunt) {
		this.currCunt = currCunt;
	}

	public String getCurrPin() {
		return currPin;
	}

	public void setCurrPin(String currPin) {
		this.currPin = currPin;
	}

	public String getPerCity() {
		return perCity;
	}

	public void setPerCity(String perCity) {
		this.perCity = perCity;
	}

	public String getPerState() {
		return perState;
	}

	public void setPerState(String perState) {
		this.perState = perState;
	}

	public String getPerCunt() {
		return perCunt;
	}

	public void setPerCunt(String perCunt) {
		this.perCunt = perCunt;
	}

	public String getPerPin() {
		return perPin;
	}

	public void setPerPin(String perPin) {
		this.perPin = perPin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getErpId() {
		return erpId;
	}

	public void setErpId(String erpId) {
		this.erpId = erpId;
	}

	public String getOffEmail() {
		return offEmail;
	}

	public void setOffEmail(String offEmail) {
		this.offEmail = offEmail;
	}

	public String getPerEmail() {
		return perEmail;
	}

	public void setPerEmail(String perEmail) {
		this.perEmail = perEmail;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getWhatsNumber() {
		return whatsNumber;
	}

	public void setWhatsNumber(String whatsNumber) {
		this.whatsNumber = whatsNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getExpInd() {
		return expInd;
	}

	public void setExpInd(String expInd) {
		this.expInd = expInd;
	}

	public String getExpAcd() {
		return expAcd;
	}

	public void setExpAcd(String expAcd) {
		this.expAcd = expAcd;
	}

	public String getDoj() {
		return doj;
	}

	public void setDoj(String doj) {
		this.doj = doj;
	}

	public String getDol() {
		return dol;
	}

	public void setDol(String dol) {
		this.dol = dol;
	}

	public String getPerAdd() {
		return perAdd;
	}

	public void setPerAdd(String perAdd) {
		this.perAdd = perAdd;
	}

	public String getCurAdd() {
		return curAdd;
	}

	public void setCurAdd(String curAdd) {
		this.curAdd = curAdd;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public String getScopusId() {
		return scopusId;
	}

	public void setScopusId(String scopusId) {
		this.scopusId = scopusId;
	}

	public String getSciId() {
		return sciId;
	}

	public void setSciId(String sciId) {
		this.sciId = sciId;
	}

}
