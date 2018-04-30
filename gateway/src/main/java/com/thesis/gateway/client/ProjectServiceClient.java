package com.thesis.gateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.thesis.gateway.dto.StatisticsDto;

@FeignClient(name = "project-service")
public interface ProjectServiceClient {

	@RequestMapping(method = RequestMethod.GET, value = "/stat")
	public StatisticsDto getStatFromProjects();
	
}
