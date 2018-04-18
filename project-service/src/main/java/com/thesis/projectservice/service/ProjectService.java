package com.thesis.projectservice.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thesis.projectservice.domain.Issue;
import com.thesis.projectservice.domain.Project;
import com.thesis.projectservice.dto.IssueDto;
import com.thesis.projectservice.dto.ProjectDto;
import com.thesis.projectservice.repository.CommentRepository;
import com.thesis.projectservice.repository.IssueRepository;
import com.thesis.projectservice.repository.ProjectRepository;

@Service
public class ProjectService {

	private static Logger log = LoggerFactory.getLogger(ProjectService.class);

	@Autowired
	private ProjectRepository projectRepo;

	@Autowired
	private IssueRepository issueRepo;

	@Autowired
	private CommentRepository commentRepo;
	
	
	public List<ProjectDto> getAllProjects() {
		List<ProjectDto> projectDtos = new ArrayList<>();

		List<Project> projects = projectRepo.findAll();
		if (null != projects) {
			for (Project p : projects) {
				ProjectDto dto = new ProjectDto();
				dto.setId(p.getId());
				dto.setName(p.getName());
				dto.setType(p.getType());
				dto.setLeader(p.getLeadId());
				projectDtos.add(dto);
			}
		}
		return projectDtos;
	}

	public void createProject(ProjectDto projectDto) {
		Project project = null;
		if (null == projectDto.getId() || null == projectRepo.findById(projectDto.getId())) {
			project = new Project();
		} else {
			project = projectRepo.findById(projectDto.getId()).get();
		}
		project.setName(projectDto.getName());
		project.setType(projectDto.getType());
		System.out.println("project leader: " + projectDto.getLeader());
		projectRepo.saveAndFlush(project);
		log.debug("Project succesfully created/updated, name: " + project.getName());
	}

	public ProjectDto getProjectDetails(Long id) {
		Project p = projectRepo.getOne(id);
		ProjectDto dto = new ProjectDto();
		if(null != p){
			dto.setId(p.getId());
			dto.setLeader(p.getLeadId());
			dto.setName(p.getName());
			dto.setType(p.getType());
			dto.setSummary(p.getSummary());
		}
		List<Issue> issues = p.getIssues();
		
		if(!issues.isEmpty()){
			List<IssueDto> items = new ArrayList<>();
			for(Issue i : issues){
				IssueDto idto = new IssueDto();
				createIssueDto(items, i, idto);
			}
			dto.setItems(items);
		}
		return dto;
	}

	private void createIssueDto(List<IssueDto> items, Issue i, IssueDto idto) {
		idto.setAssignee(i.getAssignee());
		idto.setStatus(i.getStatus());
		idto.setReporter(i.getReporter());
		idto.setAssignee(i.getAssignee());
		idto.setPriority(i.getPriority());
		idto.setType(i.getType());
		items.add(idto);
	}
	
	public void createIssue(IssueDto issueDto) {
		Issue issue = null;
		
		if (null == issueDto.getId() || null == issueRepo.findById(issueDto.getId())) {
			issue = new Issue();
			
			
		} else {
			issue = issueRepo.findById(issueDto.getId()).get();
		}
		Project p = projectRepo.getOne(issueDto.getProjectId());
		issue.setProject(p);
		issue.setName(issueDto.getName());
		issue.setType(issueDto.getType());
		issue.setPriority(issueDto.getPriority());
		issue.setReporter(issueDto.getReporter());
		issue.setStatus(issueDto.getStatus());
		issue.setSummary(issueDto.getSummary());
		System.out.println("issue assignee: " + issueDto.getAssignee());
		issueRepo.saveAndFlush(issue);
		log.debug("Project succesfully created/updated, name: " + issue.getName());
	}

}
