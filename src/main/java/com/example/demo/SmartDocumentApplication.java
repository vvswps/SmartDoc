package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartDocumentApplication {

	public static void main(String[] args) {
		// access the environment variable ${AZURE_DB_URL} and print out its last 4
		// characters
		System.out.println("Look at me \n\n\n\n\n\n\n\n\n\n");
		System.out.println(
				"AZURE_DB_URL: " + System.getenv("AZURE_DB_URL").substring(System.getenv("AZURE_DB_URL").length() - 8));
		System.out.println("\n\n\n\n\n\n\n\n\n\n Bye");

		SpringApplication.run(SmartDocumentApplication.class, args);
	}

}
