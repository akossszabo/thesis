package com.thesis.projectservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thesis.projectservice.repository.CommentRepository;
import com.thesis.projectservice.repository.IssueRepository;
import com.thesis.projectservice.repository.ProjectRepository;

@Service
public class ProjectService {

	
	@Autowired
	private ProjectRepository projectRepo;
	
	@Autowired 
	private IssueRepository issueRepo;
	
	@Autowired 
	private CommentRepository commentRepo;
	
	
}
