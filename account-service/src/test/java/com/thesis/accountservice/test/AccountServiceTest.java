package com.thesis.accountservice.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.mail.EmailException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.thesis.accountservice.domain.Account;
import com.thesis.accountservice.dto.AccountDto;
import com.thesis.accountservice.repository.AccountRepository;
import com.thesis.accountservice.service.AccountService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

	@Autowired
	private AccountService service;

	@Autowired
	private AccountRepository repo;

	@Test
	public void testSuccessfulRegistration() throws EmailException {
		AccountDto registrationRequest = new AccountDto();
		registrationRequest.setEmail("tesztelek@gmail.com");
		registrationRequest.setFirstName("Elek");
		registrationRequest.setLastName("Teszt");
		registrationRequest.setRole("user");

		service.createAccount(registrationRequest);

		Account account = repo.findByEmail("tesztelek@gmail.com");
		
		assertNotNull(account);
		assertEquals("tesztelek@gmail.com", account.getEmail());
		assertEquals("Elek",account.getFirstName());
		assertEquals("Teszt",account.getLastName());
		assertEquals("user",account.getRole());
		
		repo.delete(account);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testViolationExceptionWhileRegistration() throws EmailException {
		AccountDto registrationRequest = new AccountDto();
		registrationRequest.setEmail("tesztelek@gmail.com");
		registrationRequest.setFirstName("Elek");
		registrationRequest.setLastName("Teszt");
		registrationRequest.setRole("user");

		service.createAccount(registrationRequest);

		Account account = repo.findByEmail("tesztelek@gmail.com");
		
		assertNotNull(account);
		assertEquals("tesztelek@gmail.com", account.getEmail());
		assertEquals("Elek",account.getFirstName());
		assertEquals("Teszt",account.getLastName());
		assertEquals("user",account.getRole());
		
		service.createAccount(registrationRequest);
		repo.delete(account);
	}
}
