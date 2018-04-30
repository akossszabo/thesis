package com.thesis.projectservice.dto;

import java.util.List;

public class DeleteProjectsRequestDto {
	private List<Long> projectIds;


	public List<Long> getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(List<Long> projectIds) {
		this.projectIds = projectIds;
	}
	
}
