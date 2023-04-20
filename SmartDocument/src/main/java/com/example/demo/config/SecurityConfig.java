package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider getDaoAuthProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());

		return daoAuthenticationProvider;
	}

	//protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//auth.authenticationProvider(getDaoAuthProvider());
	//}

	
	
	//@SuppressWarnings("deprecation")
	// @SuppressWarnings("deprecation")
	@Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 http.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/student/**").hasRole("STUDENT")
			.requestMatchers("/**").permitAll().and().formLogin().loginPage("/signin").loginProcessingUrl("/login")
			.defaultSuccessUrl("/user/").and().csrf().disable();
		 http.authenticationProvider(getDaoAuthProvider());
		 
	        return http.build();
	        
	        
	        
	    }


}
