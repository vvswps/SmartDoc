package com.example.demo.config;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.UserDtls;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@Transactional
public class CustomAuthenticationFailureHandler
        extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepo;

    private static final int MAX_LOGIN_RETRIES = 3;

    public CustomAuthenticationFailureHandler() {
        super("/signin");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
        try {
            String username = request.getParameter("username");

            System.out.println("\u001B[33m" + "CustomAuthenticationFailureHandler: username: " + username
                    + "\u001B[0m");
            UserDtls user = userRepo.findByEmail(username);
            System.out.println(
                    "\u001B[33m" + "CustomAuthenticationFailureHandler: user: " + user.getName() + "\u001B[0m");
            int failedLoginAttempts = user.getFailedLoginAttempts();

            if (failedLoginAttempts >= MAX_LOGIN_RETRIES || user.isLocked()) {
                user.setLocked(true);
                userRepo.save(user);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());

                response.getOutputStream()
                        .println("Account locked due to too many failed login attempts...Contact Admin");
            } else {

                user.setFailedLoginAttempts(failedLoginAttempts + 1);
                userRepo.save(user);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());

                response.getOutputStream().println("Invalid username/password (Attempt #"
                        + (failedLoginAttempts + 1) + " out of " + MAX_LOGIN_RETRIES + ")");
            }

        } catch (Exception e) {
            System.out.println(
                    "Exception in CustomAuthenticationFailureHandler: \u001B[33m" + e.getMessage() + "\u001B[0m");
            response.getOutputStream().println("Some error occurred...Contact Admin");
        }

    }
}
