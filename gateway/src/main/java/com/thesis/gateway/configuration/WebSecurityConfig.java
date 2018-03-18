package com.thesis.gateway.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import com.thesis.gateway.client.AccountServiceClient;
import com.thesis.gateway.dto.AccountDto;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter  {

	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;
	
	@Autowired
	private AccountServiceClient accServiceClient;
	
	@Autowired
    private BCryptPasswordEncoder encoder;
	
	@Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/", "/*.*","/signup/**","/signin/**","/login*","/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin().loginPage("/login").defaultSuccessUrl("/",true)
		.permitAll()
		.and()
		.logout()
		.logoutSuccessHandler(logoutSuccessHandler)
		.permitAll();

		http.csrf().disable();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(new AuthenticationProvider() {
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				String email = authentication.getName();
				String password = authentication.getCredentials().toString();
				System.out.println("email: " + email+ ", pw: " + password);
				List<GrantedAuthority> grantedAuths = new ArrayList<>();
				AccountDto account = accServiceClient.getAccountByEmail(email);
				if (account != null) {
					if (account.getEmail().equalsIgnoreCase(email) && encoder.matches(password,account.getPassword()) ) {
						grantedAuths.add(new SimpleGrantedAuthority(account.getRole()));
						Authentication auth = new UsernamePasswordAuthenticationToken(account.getEmail(), password, grantedAuths);
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
