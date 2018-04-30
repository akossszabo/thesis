package com.thesis.projectservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.thesis.projectservice.dto.AccountDto;

@FeignClient(name = "gateway")
public interface GatewayClient {

	@RequestMapping(method = RequestMethod.GET, value = "/principal")
	public AccountDto getAccount();

}