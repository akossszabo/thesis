package com.thesis.accountservice.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.thesis.accountservice.controller.AccountController;
import com.thesis.accountservice.domain.Account;
import com.thesis.accountservice.dto.AccountDto;
import com.thesis.accountservice.dto.AccountEmailNameDto;
import com.thesis.accountservice.dto.PasswordRequest;
import com.thesis.accountservice.repository.AccountRepository;

@Service
public class AccountService {

	private static Logger log = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private EmailSender emailSender;

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@PostConstruct
	public void initAdmin() {
		Account admin = new Account();
		admin.setPassword(encoder.encode("pmadmin2018"));
		admin.setEmail("thesiselte@gmail.com");
		admin.setFirstName("Pm");
		admin.setLastName("Admin");
		admin.setRole("admin");
		if (null == accountRepo.findByEmail(admin.getEmail())) {
			accountRepo.saveAndFlush(admin);
		}
	}

	@Autowired
	private BCryptPasswordEncoder encoder;

	public void createAccount(AccountDto account) throws EmailException {
		Account acc = null;
		if (null == account.getId() || null == accountRepo.findById(account.getId())) {
			acc = new Account();
		} else {
			acc = accountRepo.findById(account.getId()).get();
		}
		acc.setEmail(account.getEmail());
		acc.setLastName(account.getLastName());
		acc.setFirstName(account.getFirstName());
		acc.setUsername(generateUsername(account.getFirstName(), account.getLastName()));
		String password = generateRandomPassword();
		acc.setPassword(encoder.encode(password));
		acc.setRole(account.getRole());
		accountRepo.saveAndFlush(acc);
		sendMail(acc, password);

		log.info("Account succesfully created/updated, username: " + acc.getFirstName() + ", id: " + acc.getId()
				+ " password: " + password);
	}

	private void sendMail(Account acc, String password) throws EmailException {
		emailSender.sendEmail(acc.getEmail(), "ProjectManager account", "Dear " + acc.getFirstName()
				+ ", \n your ProjectManager password is: " + password
				+ ". \n \n You can change it after your first login. \n \n Thank you, \n Project Manager Admin");
	}

	private String generateRandomPassword() {
		final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 8; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	private String generateUsername(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString();
	}

	public List<AccountEmailNameDto> getAllAccountsToSelect() {
		List<AccountEmailNameDto> accounts = new ArrayList<>();
		List<Account> accs = accountRepo.findAll();
		if (null != accs) {
			for (Account ac : accs) {
				AccountEmailNameDto dto = new AccountEmailNameDto();
				dto.setId(ac.getId());
				dto.setKey(ac.getEmail());
				dto.setValue(ac.getFirstName() + " " + ac.getLastName());
				accounts.add(dto);
			}
		}
		return accounts;
	}

	public List<AccountDto> getAllAccounts() {
		List<AccountDto> accounts = new ArrayList<>();
		List<Account> accs = accountRepo.findAll();
		if (null != accs) {
			for (Account ac : accs) {
				AccountDto dto = new AccountDto();
				dto.setId(ac.getId());
				dto.setEmail(ac.getEmail());
				dto.setFirstName(ac.getFirstName());
				dto.setLastName(ac.getLastName());
				dto.setRole(ac.getRole());
				accounts.add(dto);
			}
		}
		return accounts;
	}

	public void removeAccounts(List<Long> ids) {
		List<Account> accounts = accountRepo.findAllById(ids);
		if (null != accounts) {
			accountRepo.deleteAll(accounts);
		}
	}

	public AccountDto getAccountByEmail(String email) {
		AccountDto dto = new AccountDto();
		System.out.println("beesett: " + email);
		Account account = accountRepo.findByEmail(email);
		if (null != account) {
			dto.setFirstName(account.getFirstName());
			dto.setLastName(account.getLastName());
			dto.setEmail(account.getEmail());
			dto.setPassword(account.getPassword());
			dto.setRole(account.getRole());
		} else {
			return null;
		}
		return dto;
	}

	public Long getUserNumber() {
		return accountRepo.count();
	}

	public void modifyPassword(PasswordRequest pwRequest) {

		Account acc = accountRepo.findByEmail(pwRequest.getUserEmail());

		if (null != acc) {
			log.info("account not null");
			log.info("password matches");
			acc.setPassword(encoder.encode(pwRequest.getPassword()));
			accountRepo.saveAndFlush(acc);
		}
	}
}
