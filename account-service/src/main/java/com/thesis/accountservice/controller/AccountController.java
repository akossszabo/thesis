package com.thesis.accountservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thesis.accountservice.dto.BaseResponse;
import com.thesis.accountservice.dto.RegistrationRequestDto;
import com.thesis.accountservice.service.AccountService;

@RestController
public class AccountController {

	private static Logger log = LoggerFactory.getLogger(AccountController.class);
	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public BaseResponse hello(@RequestBody RegistrationRequestDto accounts) {
		BaseResponse response = new BaseResponse();
		try {
			accountService.createAccount(accounts);
			response.setMessage("New account succesfully created!");
		} catch (Throwable t) {
			log.error("error while creating new account", t);
			response.setMessage("Something went wrong!");
		}
		return response;
	}
}
