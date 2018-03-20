package com.thesis.gateway.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thesis.gateway.dto.AccountDto;
import com.thesis.gateway.service.GatewayService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class GatewayController {

	@Autowired
	private GatewayService gatewayService;

	@RequestMapping(value = "/principal", method = RequestMethod.GET)
	public AccountDto getPrincipal(Authentication auth, HttpServletResponse response) {
		AccountDto dto = new AccountDto();
		try {
			dto = gatewayService.getAccountInfo(auth);
		} catch (Throwable t) {
			dto.setMessage("User not found!");
			response.setStatus(404);
		}
		return dto;
	}
}
