package com.thesis.projectservice.dto;

import java.util.Date;
import java.util.List;

public class ProjectDto extends BaseResponse{
	
	private Long id;
	private String name;
	private String type;
	private String leader;
	private List<IssueDto> items;
	private String summary;
	private Date creationDate;
	private String state;
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public List<IssueDto> getItems() {
		return items;
	}
	public void setItems(List<IssueDto> items) {
		this.items = items;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	
}
