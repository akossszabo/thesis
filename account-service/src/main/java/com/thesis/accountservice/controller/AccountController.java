package com.thesis.accountservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thesis.accountservice.dto.AccountDto;
import com.thesis.accountservice.dto.AccountsResponseDto;
import com.thesis.accountservice.dto.BaseResponse;
import com.thesis.accountservice.dto.DeleteAccountsRequestDto;
import com.thesis.accountservice.dto.RegistrationRequestDto;
import com.thesis.accountservice.service.AccountService;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class AccountController {

	private static Logger log = LoggerFactory.getLogger(AccountController.class);
	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public BaseResponse registration(@RequestBody RegistrationRequestDto request) {
		BaseResponse response = new BaseResponse();
		try {
			accountService.createAccount(request.getAccount());
			log.debug("New account succesfully created!");
			response.setMessage("New account succesfully created!");
		} catch (Throwable t) {
			log.error("error while creating new account", t);
			response.setMessage("Something went wrong!");
		}
		return response;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public BaseResponse delete(@RequestBody DeleteAccountsRequestDto request) {
		BaseResponse response = new BaseResponse();
		try {
			accountService.removeAccounts(request.getAccountIds());
			log.debug("Accounts succesfully deleted, ids: " + request.getAccountIds());
			response.setMessage("Account(s) deleted.");
		} catch (Throwable t) {
			log.error("error while remove accounts", t);
			response.setMessage("Something went wrong!");
		}
		return response;
	}
	
	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
	public AccountsResponseDto getAllAccounts() {
		AccountsResponseDto response = new AccountsResponseDto();
		try {
			response.setItems(accountService.getAllAccounts());
			log.debug("get all accounts");
		} catch (Throwable t) {
			log.error("error while listing accounts", t);
			response.setMessage("Something went wrong!");
		}
		return response;
	}
	
	@RequestMapping(value = "/principalinfo", method = RequestMethod.GET)
	public AccountDto getAccountByEmail(@RequestParam("email") String email) {
		AccountDto response = new AccountDto();
		try {
			response = accountService.getAccountByEmail(email);
			log.debug("get all accounts");
		} catch (Throwable t) {
			log.error("error while listing accounts", t);
			response.setMessage("Something went wrong!");
		}
		return response;
	}
}
