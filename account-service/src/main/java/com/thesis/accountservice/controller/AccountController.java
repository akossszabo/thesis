package com.thesis.accountservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

	private static Logger log = LoggerFactory.getLogger(AccountController.class);
	
	@RequestMapping(value = "/hello", method= RequestMethod.GET)
	public String hello() {
		log.debug("HELLLOOOOOOOOOOOO");
		return "hello";
		
	}
}
