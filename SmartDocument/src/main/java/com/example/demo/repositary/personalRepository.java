package com.example.demo.repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.PersonalDtls;


public interface personalRepository extends JpaRepository<PersonalDtls,Integer> {

}
