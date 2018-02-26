package com.thesis.accountservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thesis.accountservice.dto.AccountDto;
import com.thesis.accountservice.repository.AccountRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepo;
	
	
	public void createAccount(AccountDto acc) {
		
		
	}
	
}
