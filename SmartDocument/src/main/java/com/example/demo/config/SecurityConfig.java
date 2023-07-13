package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	public AuthenticationSuccessHandler customSuccessHandler;

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider getDaoAuthProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());

		return daoAuthenticationProvider;
	}

	// protected void configure(AuthenticationManagerBuilder auth) throws Exception
	// {
	// auth.authenticationProvider(getDaoAuthProvider());
	// }

	// @SuppressWarnings("deprecation")
	// @SuppressWarnings("deprecation")

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/student/**")
				.hasRole("student").requestMatchers("/teacher/**").hasRole("TEACHER").requestMatchers("/hod/**").hasRole("HOD")
				.requestMatchers("/**").permitAll().and().formLogin().loginPage("/signin").loginProcessingUrl("/login")
				.successHandler(customSuccessHandler).and().csrf().disable();
		http.authenticationProvider(getDaoAuthProvider());
		http.headers().frameOptions().sameOrigin();
		http.rememberMe().tokenValiditySeconds(60 * 60 * 7).key("11T01:54:59.903+05:30DEBUG36336")
				.rememberMeParameter("remember-me")
				.rememberMeCookieName("rememberlogin").and().logout().logoutUrl("/logout")
				.logoutSuccessUrl("/signin?logout").invalidateHttpSession(true).deleteCookies("JSESSIONID");

		return http.build();

	}

}
