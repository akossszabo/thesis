package com.thesis.accountservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.thesis.accountservice.controller.AccountController;
import com.thesis.accountservice.domain.Account;
import com.thesis.accountservice.dto.AccountDto;
import com.thesis.accountservice.repository.AccountRepository;

@Service
public class AccountService implements UserDetailsService {

	private static Logger log = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account user = accountRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return user;
	}


	public void createAccount(AccountDto account) {
		Account acc = new Account();
		acc.setEmail(account.getEmail());
		acc.setLastName(account.getLastName());
		acc.setFirstName(account.getFirstName());
		acc.setUsername(generateUsername(account.getFirstName(), account.getLastName()));
		String password = generateRandomPassword();
		acc.setPassword(encoder.encode(password));
		accountRepo.saveAndFlush(acc);
		log.debug("new account succesfully created, username: " + acc.getUsername());
	}

	private String generateRandomPassword() {
		// TODO Auto-generated method stub
		return "TESZT";
	}

	private String generateUsername(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return "username";
	}
}
