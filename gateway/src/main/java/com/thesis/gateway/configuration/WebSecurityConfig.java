package com.thesis.gateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;

import com.thesis.gateway.client.AccountServiceClient;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

	@Autowired
	private AccountServiceClient accServiceClient;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
		//@formatter:off
		return http
				.authorizeExchange()
				.anyExchange()
				//.permitAll()
				.authenticated()
				.and()
				.formLogin()
				.and()
				.csrf().disable()
				.headers()
				.frameOptions().mode(Mode.SAMEORIGIN)
				.and().and().build();
		//@formatter:on
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
