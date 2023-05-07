package com.example.demo.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class UserDtls {

	@Override
	public String toString() {
		return "UserDtls(id=" + id + ", name=" + name + ", branch=" + branch + ", email=" + email + ", password="
				+ password + ", role=" + role + ", mobileNumber=" + mobileNumber + ", files=" + files + ")";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	private String branch;

	@Column(nullable = false, unique = true)
	private String email;

	private String password;

	private String role;
	private String mobileNumber;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<DatabaseFile> files;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private PersonalDtls personalDtls;

	public PersonalDtls getPersonalDtls() {
		return personalDtls;
	}

	public void setPersonalDtls(PersonalDtls personalDtls) {
		this.personalDtls = personalDtls;
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

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
