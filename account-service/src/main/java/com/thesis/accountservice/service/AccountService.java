package com.thesis.accountservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.thesis.accountservice.controller.AccountController;
import com.thesis.accountservice.domain.Account;
import com.thesis.accountservice.dto.AccountDto;
import com.thesis.accountservice.repository.AccountRepository;

@Service
public class AccountService{

	private static Logger log = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountRepository accountRepo;

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	private BCryptPasswordEncoder encoder;


	public void createAccount(AccountDto account) {
		Account acc = null;
		if(null == account.getId() || null == accountRepo.findOne(account.getId())) {
			acc = new Account();
		}else {
			acc = accountRepo.findOne(account.getId());			
		}
		acc.setEmail(account.getEmail());
		acc.setLastName(account.getLastName());
		acc.setFirstName(account.getFirstName());
		acc.setUsername(generateUsername(account.getFirstName(), account.getLastName()));
		String password = generateRandomPassword();
		acc.setPassword(encoder.encode(password));
		accountRepo.saveAndFlush(acc);
		log.debug("Account succesfully created/updated, username: " + acc.getUsername()+ ", id: " + acc.getId());
	}

	private String generateRandomPassword() {
		// TODO Auto-generated method stub
		return "TESZT";
	}

	private String generateUsername(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString();
	}

	public List<AccountDto> getAllAccounts() {
		List<AccountDto> accounts = new ArrayList<>();
		List<Account> accs = accountRepo.findAll();
		if(null!= accs) {
			for(Account ac : accs) {
				AccountDto dto = new AccountDto();
				dto.setId(ac.getId());
				dto.setEmail(ac.getEmail());
				dto.setFirstName(ac.getFirstName());
				dto.setLastName(ac.getLastName());
				accounts.add(dto);
			}
		}
		return accounts;
	}
}
