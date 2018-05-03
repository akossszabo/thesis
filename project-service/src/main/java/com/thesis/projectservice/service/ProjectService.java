package com.thesis.projectservice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thesis.projectservice.client.AccountServiceClient;
import com.thesis.projectservice.client.GatewayClient;
import com.thesis.projectservice.domain.Comment;
import com.thesis.projectservice.domain.Issue;
import com.thesis.projectservice.domain.Project;
import com.thesis.projectservice.dto.AccountDto;
import com.thesis.projectservice.dto.CommentDto;
import com.thesis.projectservice.dto.IssueDto;
import com.thesis.projectservice.dto.IssuesResponseDto;
import com.thesis.projectservice.dto.ProjectDto;
import com.thesis.projectservice.dto.StatisticsDto;
import com.thesis.projectservice.repository.CommentRepository;
import com.thesis.projectservice.repository.IssueRepository;
import com.thesis.projectservice.repository.ProjectRepository;

@Service
public class ProjectService {

	private static Logger log = LoggerFactory.getLogger(ProjectService.class);
	@Autowired
	private GatewayClient gatewayClient;

	@Autowired
	private AccountServiceClient accountClient;

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
				dto.setCreationDate(p.getCreationDate());
				AccountDto account = accountClient.getAccountByEmail(p.getLeadId());
				dto.setLeader(account.getFirstName() + " " + account.getLastName());
				projectDtos.add(dto);
			}
		}
		return projectDtos;
	}

	public Long createProject(ProjectDto projectDto) {
		Project project = null;
		if (null == projectDto.getId() || null == projectRepo.findById(projectDto.getId())) {
			project = new Project();
		} else {
			project = projectRepo.findById(projectDto.getId()).get();
		}
		project.setName(projectDto.getName());
		project.setType(projectDto.getType());
		project.setLeadId(projectDto.getLeader());
		AccountDto account = accountClient.getAccountByEmail(projectDto.getLeader());
		project.setLeaderName(account.getFirstName() + " " + account.getLastName());
		project.setCreationDate(new Date());
		project.setSummary(projectDto.getSummary());
		log.debug("Project succesfully created/updated, name: " + project.getName());
		return projectRepo.saveAndFlush(project).getId();
	}

	public ProjectDto getProjectDetails(Long id) {
		Project p = projectRepo.getOne(id);
		ProjectDto dto = new ProjectDto();
		if (null != p) {
			dto.setId(p.getId());
			dto.setName(p.getName());
			dto.setType(p.getType());
			dto.setSummary(p.getSummary());
			dto.setCreationDate(p.getCreationDate());
			dto.setLeader(p.getLeaderName());
		}
		List<Issue> issues = p.getIssues();
		List<IssueDto> items = new ArrayList<>();
		if (!issues.isEmpty()) {
			for (Issue i : issues) {
				IssueDto idto = new IssueDto();
				createIssueDto(items, i, idto);
			}
		}
		dto.setItems(items);
		return dto;
	}

	private void createIssueDto(List<IssueDto> items, Issue i, IssueDto idto) {
		idto.setAssignee(i.getAssigneeName());
		idto.setStatus(i.getStatus());
		idto.setReporter(i.getReporterName());
		idto.setPriority(i.getPriority());
		idto.setType(i.getType());
		idto.setName(i.getName());
		idto.setId(i.getId());
		idto.setCreationDate(i.getCreationDate());
		items.add(idto);
	}

	public Long createIssue(IssueDto issueDto) {
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
		AccountDto account = accountClient.getAccountByEmail(issueDto.getReporter());
		if (null != account) {
			issue.setReporterName(account.getFirstName() + " " + account.getLastName());
		}
		issue.setStatus(issueDto.getStatus());
		issue.setSummary(issueDto.getSummary());
		issue.setAssignee(issueDto.getAssignee());
		issue.setCreationDate(new Date());
		AccountDto account2 = accountClient.getAccountByEmail(issueDto.getAssignee());
		if (null != account2) {
			issue.setAssigneeName(account2.getFirstName() + " " + account2.getLastName());
		}
		log.debug("Project succesfully created/updated, name: " + issue.getName());

		return issueRepo.saveAndFlush(issue).getId();
	}

	public IssueDto geIssueDetails(Long id) {
		Issue issue = issueRepo.getOne(id);
		IssueDto dto = new IssueDto();
		dto.setAssignee(issue.getAssigneeName());
		dto.setName(issue.getName());
		dto.setPriority(issue.getPriority());
		dto.setProjectId(issue.getProject().getId());
		dto.setStatus(issue.getStatus());
		dto.setType(issue.getType());
		dto.setReporter(issue.getReporterName());
		dto.setId(issue.getId());
		dto.setCreationDate(issue.getCreationDate());
		dto.setSummary(issue.getSummary());
		dto.setComments(addCommentsToIssueDto(issue.getComments()));
		return dto;
	}

	private List<CommentDto> addCommentsToIssueDto(List<Comment> comments) {
		List<CommentDto> dtos = new ArrayList<>();
		for (Comment c : comments) {
			CommentDto dto = new CommentDto();
			dto.setMessage(c.getMessage());
			dto.setIssueId(c.getIssue().getId());
			dto.setSendDate(c.getSendDate());
			dto.setUser(c.getPerson());
			dtos.add(dto);
		}
		return dtos;
	}

	public Long addCommentToIssue(Long id, CommentDto request) {
		Issue issue = issueRepo.getOne(id);
		Comment c = new Comment();
		c.setIssue(issue);
		c.setMessage(request.getMessage());
		c.setPerson(request.getUser());
		c.setSendDate(new Date());
		return commentRepo.saveAndFlush(c).getId();
	}

	public IssuesResponseDto getAllIssues() {
		IssuesResponseDto response = new IssuesResponseDto();
		List<Issue> issues = issueRepo.findByOrderByCreationDate();
		List<IssueDto> responseItems = new ArrayList<>();
		for (Issue issue : issues) {
			IssueDto dto = new IssueDto();
			dto.setAssignee(issue.getAssigneeName());
			dto.setReporter(issue.getReporterName());
			dto.setName(issue.getName());
			dto.setPriority(issue.getPriority());
			dto.setProjectId(issue.getProject().getId());
			dto.setStatus(issue.getStatus());
			dto.setType(issue.getType());
			dto.setId(issue.getId());
			dto.setCreationDate(issue.getCreationDate());
			dto.setComments(addCommentsToIssueDto(issue.getComments()));
			responseItems.add(dto);
		}
		response.setItems(responseItems);
		return response;
	}

	public StatisticsDto getStatInfo() {
		StatisticsDto dto = new StatisticsDto();
		dto.setCommentNumber(commentRepo.count());
		dto.setProjectNumber(projectRepo.count());
		dto.setIssueNumber(issueRepo.count());
		return dto;
	}

	public void removeProjects(List<Long> projectIds) {
		List<Project> projects = projectRepo.findAllById(projectIds);
		for (Project p : projects) {
			for (Issue i : p.getIssues()) {
				commentRepo.deleteAll(i.getComments());
			}
			issueRepo.deleteAll(p.getIssues());
		}
		projectRepo.deleteAll(projects);
	}

	public void removeIssues(List<Long> issueIds) {
		List<Issue> issues = issueRepo.findAllById(issueIds);
		for (Issue i : issues) {
			commentRepo.deleteAll(i.getComments());
		}
		issueRepo.deleteAll(issues);

	}

	public List<AccountDto> getActiveUsers(Long projectId) {
		Project p = projectRepo.getOne(projectId);
		List<AccountDto> accounts = new ArrayList<>();
		Set<String> users = new HashSet<>();
		for (Issue i : p.getIssues()) {
			users.add(i.getAssigneeName());
		}
		Long id = 0L;
		for (String u : users) {
			AccountDto dto = new AccountDto();
			dto.setId(id);
			dto.setName(u);
			++id;
			accounts.add(dto);
		}
		return accounts;
	}

}
