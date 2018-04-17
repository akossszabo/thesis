package com.thesis.gateway.web.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.thesis.gateway.dto.AccountDto;
import com.thesis.gateway.service.GatewayService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class GatewayController {

	@Autowired
	private GatewayService gatewayService;
	@Autowired
	private DiscoveryClient dclient;

	@RequestMapping(value = "/principal", method = RequestMethod.GET)
	public AccountDto getPrincipal(Authentication auth, ServerHttpResponse response) {
		AccountDto dto = new AccountDto();
		try {
			dto = gatewayService.getAccountInfo(auth);
		} catch (Throwable t) {
			dto.setMessage("User not found!");
			response.setStatusCode(HttpStatus.NOT_FOUND);
		}
		return dto;
	}

	@RequestMapping(value = "/chatinstance", method = RequestMethod.GET)
	public @ResponseBody String getChatInstanceUri() {

		return getRandomInstance();

	}

	private String getRandomInstance() {

		List<ServiceInstance> instances = dclient.getInstances("chat-service");
		if (!instances.isEmpty()) {
			Random r = new Random();
			ServiceInstance inst = instances.get(r.nextInt(instances.size()));
			System.out.println(inst.getHost() + " " + inst.getPort() + " ");
			return inst.getUri().toString();
		}

		return "no instance";
	}

}
