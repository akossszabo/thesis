package com.thesis.projectservice.dto;

import java.util.List;

public class ProjectsResponseDto extends BaseResponse{

	private List<ProjectDto> items;

	public List<ProjectDto> getItems() {
		return items;
	}

	public void setItems(List<ProjectDto> items) {
		this.items = items;
	}
}
