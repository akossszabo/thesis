package com.thesis.accountservice.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.thesis.accountservice.domain.Account;
import com.thesis.accountservice.repository.AccountRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AccountRepository accountRepo;

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http.logout().permitAll();
		http.csrf().disable();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(new AuthenticationProvider() {

			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				String email = authentication.getName();
				String password = authentication.getCredentials().toString();
				List<GrantedAuthority> grantedAuths = new ArrayList<>();
				Account user = accountRepo.findByEmail(email);
				if (user != null) {
					if (user.getEmail().equalsIgnoreCase(email) && encoder.matches(password, user.getPassword())) {
						grantedAuths.add(new SimpleGrantedAuthority("USER"));
						Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), password,
								grantedAuths);
						return auth;
					}
				}

				return null;
			}

			@Override
			public boolean supports(Class<?> authentication) {
				return authentication.equals(UsernamePasswordAuthenticationToken.class);
			}
		});

	}

}