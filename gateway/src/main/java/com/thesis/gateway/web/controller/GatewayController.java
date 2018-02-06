package com.thesis.gateway.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thesis.gateway.web.dto.DummyDto;
import com.thesis.gateway.web.dto.ProjectDto;

@RequestMapping("/api")
@RestController
public class GatewayController {
	
	@RequestMapping("/dummy")
	public DummyDto getTest() {
		return getDummyData();
	}

	
	
	private DummyDto getDummyData() {
		DummyDto dummy = new DummyDto();
		List<ProjectDto> items = new ArrayList<>();
		ProjectDto project = new ProjectDto();
		project.setName("Dummy Project");
		project.setCompany("Dummy Company");
		items.add(project);
		dummy.setItems(items);
		return dummy;
	}
}
