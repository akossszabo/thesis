package com.thesis.projectservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.thesis.projectservice.domain.Comment;
import com.thesis.projectservice.domain.Issue;
import com.thesis.projectservice.domain.Project;
import com.thesis.projectservice.dto.CommentDto;
import com.thesis.projectservice.dto.IssueDto;
import com.thesis.projectservice.dto.ProjectDto;
import com.thesis.projectservice.repository.CommentRepository;
import com.thesis.projectservice.repository.IssueRepository;
import com.thesis.projectservice.repository.ProjectRepository;
import com.thesis.projectservice.service.ProjectService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectServiceApplicationTests {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectRepository projectRepo;

	@Autowired
	private CommentRepository commentRepo;

	@Autowired
	private IssueRepository issueRepo;

	@Test
	@Transactional
	public void testProjectCreation() {
		ProjectDto p = new ProjectDto();
		Date creationDate = new Date();
		p.setCreationDate(creationDate);
		p.setLeader("Test Leader");
		p.setName("Test project");
		p.setSummary("This is the summary of the Test project");
		p.setType("Business");

		Long id = projectService.createProject(p);

		Project project = projectRepo.getOne(id);

		assertNotNull(project);
		assertEquals("Test Leader", project.getLeadId());
		assertEquals("Test project", project.getName());
		assertEquals("This is the summary of the Test project", project.getSummary());
		assertEquals("Business", project.getType());
		assertEquals(creationDate, project.getCreationDate());

		projectRepo.delete(project);
	}

	@Test
	@Transactional
	public void testIssueCreation() {
		ProjectDto p = new ProjectDto();
		Date creationDate = new Date();
		p.setCreationDate(creationDate);
		p.setLeader("Test Leader");
		p.setName("Test project");
		p.setSummary("This is the summary of the Test project");
		p.setType("Business");

		Long projectId = projectService.createProject(p);

		IssueDto iDto = new IssueDto();
		iDto.setAssignee("Test Assignee");
		iDto.setName("Test Issue");
		iDto.setProjectId(projectId);
		iDto.setSummary("This is the summary of Test Issue");
		iDto.setReporter("Test Reporter");
		iDto.setType("New Feature");
		Long issueId = projectService.createIssue(iDto);

		Issue issue = issueRepo.getOne(issueId);
		assertNotNull(issue);
		assertEquals(projectId, issue.getProject().getId());
		assertEquals("This is the summary of Test Issue", issue.getSummary());
		assertEquals("Test Reporter", issue.getReporter());
		assertEquals("New Feature", issue.getType());
		issueRepo.delete(issue);
		projectRepo.deleteById(projectId);
	}

	@Test
	@Transactional
	public void testCommentCreation() {
		ProjectDto p = new ProjectDto();
		Date creationDate = new Date();
		p.setCreationDate(creationDate);
		p.setLeader("Test Leader");
		p.setName("Test project");
		p.setSummary("This is the summary of the Test project");
		p.setType("Business");

		Long projectId = projectService.createProject(p);
		IssueDto iDto = new IssueDto();
		iDto.setAssignee("Test Assignee");
		iDto.setName("Test Issue");
		iDto.setProjectId(projectId);
		iDto.setSummary("This is the summary of Test Issue");
		iDto.setReporter("Test Reporter");
		iDto.setType("New Feature");
		Long issueId = projectService.createIssue(iDto);

		Issue issue = issueRepo.getOne(issueId);

		CommentDto dto = new CommentDto();
		dto.setIssueId(issue.getId());
		dto.setMessage("Test Comment");
		dto.setUser("Test User");
		dto.setSendDate(new Date());
		Long commentId = projectService.addCommentToIssue(issue.getId(), dto);
		Comment c = commentRepo.getOne(commentId);
		assertNotNull(c);
		assertEquals("Test Comment", c.getMessage());
		assertEquals("Test User", c.getPerson());
		assertEquals(issueId, c.getIssue().getId());

		commentRepo.deleteById(commentId);
		issueRepo.delete(issue);
		projectRepo.deleteById(projectId);
	}

}
