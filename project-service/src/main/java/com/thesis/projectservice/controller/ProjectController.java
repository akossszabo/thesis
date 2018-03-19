package com.thesis.projectservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thesis.projectservice.dto.BaseResponse;
import com.thesis.projectservice.dto.CreateProjectRequestDto;
import com.thesis.projectservice.dto.ProjectsResponseDto;
import com.thesis.projectservice.service.ProjectService;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class ProjectController {

	private static Logger log = LoggerFactory.getLogger(ProjectController.class);
	
	@Autowired
	private ProjectService projectService;
	
	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public ProjectsResponseDto getAllProjects() {
		ProjectsResponseDto response = new ProjectsResponseDto();
		try {
			response.setItems(projectService.getAllProjects());
			log.debug("get all accounts");
		} catch (Throwable t) {
			log.error("error while listing accounts", t);
			response.setMessage("Something went wrong!");
		}
		return response;
	}
	
	@RequestMapping(value = "/projects", method = RequestMethod.POST)
	public BaseResponse createProject(@RequestBody CreateProjectRequestDto request) {
		BaseResponse response = new BaseResponse();
		try {
			projectService.createProject(request.getProject());
			response.setMessage("Project succesfully created! ");
		} catch (Throwable t) {
			log.error("error while listing accounts", t);
			response.setMessage("Something went wrong!");
		}
		return response;
	}
}
