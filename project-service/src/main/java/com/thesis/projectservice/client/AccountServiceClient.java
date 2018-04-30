package com.thesis.projectservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.thesis.projectservice.dto.AccountDto;


@FeignClient(name = "account-service")
public interface AccountServiceClient {

	@RequestMapping(method = RequestMethod.GET, value = "/principalinfo")
	public AccountDto getAccountByEmail(@RequestParam("email") String email);

}
