package com.thesis.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.thesis.gateway.client.AccountServiceClient;
import com.thesis.gateway.dto.AccountDto;

import reactor.core.publisher.Mono;

@Component
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

	@Autowired
	private AccountServiceClient accountServiceClient;

	@Override
	public Mono<UserDetails> findByUsername(String email) {
		return getAccount(email).cast(UserDetails.class);
	}

	private Mono<AccountDto> getAccount(String email) {
		return Mono.justOrEmpty(accountServiceClient.getAccountByEmail(email));
	}
}
