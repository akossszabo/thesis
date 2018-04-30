package com.thesis.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.thesis.gateway.client.AccountServiceClient;
import com.thesis.gateway.client.ProjectServiceClient;
import com.thesis.gateway.dto.AccountDto;
import com.thesis.gateway.dto.StatisticsDto;

@Service
public class GatewayService {

	@Autowired
	private AccountServiceClient accountClient;
	
	@Autowired
	private ProjectServiceClient projectClient;

	public AccountDto getAccountInfo(Authentication auth) {
		AccountDto dto = accountClient.getAccountByEmail(auth.getName());
		return dto;
	}

	public StatisticsDto getStatisticsInfo() {
		StatisticsDto response = projectClient.getStatFromProjects();
		response.setUserNumber(accountClient.getUserNumber());
		return response;
	}
}
