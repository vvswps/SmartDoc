package com.example.demo.service;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class PasswordValidatorService {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 30;
    private static final Pattern UPPER_CASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWER_CASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]");

    public String validatePasswordAndEmail(String password, String email) {
        if (!isValidPassword(password)) {
            return "Password should have at least 8 characters, including uppercase, lowercase, digit, and special character.";
        }

        if (password.equals(email) || password.equalsIgnoreCase(email)) {
            return "Password cannot be the same as the email.";
        }

        return null; // No issues found
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            return false;
        }

        boolean hasUpperCase = UPPER_CASE_PATTERN.matcher(password).find();
        boolean hasLowerCase = LOWER_CASE_PATTERN.matcher(password).find();
        boolean hasDigit = DIGIT_PATTERN.matcher(password).find();
        boolean hasSpecialChar = SPECIAL_CHAR_PATTERN.matcher(password).find();

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }
}
