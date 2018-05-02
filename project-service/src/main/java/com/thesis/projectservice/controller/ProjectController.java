package com.thesis.projectservice.controller;


import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thesis.projectservice.dto.AccountsResponseDto;
import com.thesis.projectservice.dto.BaseResponse;
import com.thesis.projectservice.dto.CommentDto;
import com.thesis.projectservice.dto.CreateIssueRequestDto;
import com.thesis.projectservice.dto.CreateProjectRequestDto;
import com.thesis.projectservice.dto.DeleteIssuesRequestDto;
import com.thesis.projectservice.dto.DeleteProjectsRequestDto;
import com.thesis.projectservice.dto.IssueDto;
import com.thesis.projectservice.dto.IssuesResponseDto;
import com.thesis.projectservice.dto.ProjectDto;
import com.thesis.projectservice.dto.ProjectsResponseDto;
import com.thesis.projectservice.dto.StatisticsDto;
import com.thesis.projectservice.service.ProjectService;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class ProjectController {

	private static Logger log = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private ProjectService projectService;

	@GetMapping("/projects")
	public ProjectsResponseDto getAllProjects() {
		ProjectsResponseDto response = new ProjectsResponseDto();
		try {
			response.setItems(projectService.getAllProjects());
			log.debug("get all projects");
		} catch (Throwable t) {
			log.error("error while listing projects", t);
			response.setMessage("Something went wrong!");
		}
		return response;
	}

	@PostMapping("/projects")
	public BaseResponse createProject(@RequestBody CreateProjectRequestDto request) {

		BaseResponse response = new BaseResponse();
		try {
			projectService.createProject(request.getProject());
			response.setMessage("Project succesfully created! ");
		} catch (Throwable t) {
			log.error("error while creating project", t);
			response.setMessage("Something went wrong!");
		}
		return response;
	}

	@GetMapping("/projects/{id}")
	public ProjectDto getProjectDetails(@PathVariable Long id) {
		ProjectDto response = new ProjectDto();
		try {
			log.debug("get project by id,", id);
			return projectService.getProjectDetails(id);
		} catch (Throwable t) {
			log.error("error while looking for project", t);
			response.setMessage("Something went wrong!");
		}
		return response;
	}
	
	@PostMapping("/issues")
	public BaseResponse createIssue(@RequestBody CreateIssueRequestDto request,HttpServletResponse httpRes) {
		BaseResponse response = new BaseResponse();
		try {
			projectService.createIssue(request.getIssue());
			response.setMessage("Issue succesfully created! ");
		} catch (Throwable t) {
			log.error("error while creating issue", t);
			response.setMessage("Something went wrong!");
		}
		httpRes.addHeader("Access-Control-Allow-Origin", "*");
		return response;
	}
	
	@GetMapping("/allissues")
	public IssuesResponseDto getAllIssues(HttpServletResponse httpRes) {
		IssuesResponseDto response = new IssuesResponseDto();
		try {
			response = projectService.getAllIssues();
			response.setMessage("Issue succesfully created! ");
		} catch (Throwable t) {
			log.error("error while creating issue", t);
			response.setMessage("Something went wrong!");
		}
		httpRes.addHeader("Access-Control-Allow-Origin", "*");
		return response;
	}
	
	@GetMapping("/issues/{id}")
	public IssueDto getIssueDetails(@PathVariable Long id) {
		IssueDto response = new IssueDto();
		try {
			log.debug("get project by id,", id);
			return projectService.geIssueDetails(id);
		} catch (Throwable t) {
			log.error("error while looking for project", t);
			response.setMessage("Something went wrong!");
		}
		return response;
	}
	
	@PostMapping("/issues/{id}")
	public BaseResponse addCommentToIssue(@PathVariable Long id, @RequestBody CommentDto request,HttpServletResponse httpRes) {
		BaseResponse response = new BaseResponse();
		try {
			projectService.addCommentToIssue(id,request);
			response.setMessage("Comment succesfully createt, issueId: " + id);
		} catch (Throwable t) {
			log.error("error while creating issue", t);
			response.setMessage("Something went wrong!");
		}
		httpRes.addHeader("Access-Control-Allow-Origin", "*");
		return response;
	}
	
	@GetMapping("/stat")
	public StatisticsDto getStatisticsInfo() {
		StatisticsDto response = new StatisticsDto();
		try {
			log.debug("get stat info");
			return projectService.getStatInfo();
		} catch (Throwable t) {
			log.error("error while looking for stat", t);
			response.setMessage("Something went wrong!");
		}
		return response;
	}
	
	@RequestMapping(value = "/deleteprojects", method = RequestMethod.POST)
	public BaseResponse delete(@RequestBody DeleteProjectsRequestDto request) {
		BaseResponse response = new BaseResponse();
		try {
			projectService.removeProjects(request.getProjectIds());
			log.debug("projects succesfully deleted, ids: " + request.getProjectIds());
			response.setMessage("projects deleted.");
		} catch (Throwable t) {
			log.error("error while remove projects", t);
			response.setMessage("Something went wrong!");
		}
		return response;
	}
	
	@RequestMapping(value = "/deleteissues", method = RequestMethod.POST)
	public BaseResponse delete(@RequestBody DeleteIssuesRequestDto request) {
		BaseResponse response = new BaseResponse();
		try {
			projectService.removeIssues(request.getIssueIds());
			log.debug("projects succesfully deleted, ids: " + request.getIssueIds());
			response.setMessage("projects deleted.");
		} catch (Throwable t) {
			log.error("error while remove issues", t);
			response.setMessage("Something went wrong!");
		}
		return response;
	}
	
	@GetMapping("/activeusers/{projectId}")
	public AccountsResponseDto getActiveUsers(@PathVariable Long projectId) {
		AccountsResponseDto response = new AccountsResponseDto();
		try {
			response.setItems(projectService.getActiveUsers(projectId));
			log.debug("getActiveUsers: " + projectId);
			response.setMessage("projects deleted.");
		} catch (Throwable t) {
			log.error("error while remove issues", t);
			response.setMessage("Something went wrong!");
		}
		return response;
	}
	
}
