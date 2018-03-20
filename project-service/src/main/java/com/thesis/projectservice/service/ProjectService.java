package com.thesis.projectservice.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thesis.projectservice.domain.Project;
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
		if (null == projectDto.getId() || null == projectRepo.findOne(projectDto.getId())) {
			project = new Project();
		} else {
			project = projectRepo.findOne(projectDto.getId());
		}
		project.setName(projectDto.getName());
		project.setType(projectDto.getType());
		System.out.println("project leader: " + projectDto.getLeader());
		projectRepo.saveAndFlush(project);
		log.debug("Project succesfully created/updated, name: " + project.getName());
	}

}
