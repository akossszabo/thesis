package com.thesis.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.thesis.gateway.client.AccountServiceClient;
import com.thesis.gateway.dto.AccountDto;

@Service
public class GatewayService {

	@Autowired
	private AccountServiceClient accountClient;

	public AccountDto getAccountInfo(Authentication auth) {
		//TODO: 
		AccountDto dto = accountClient.getAccountByEmail(auth.getName());
		return dto;
	}
}
