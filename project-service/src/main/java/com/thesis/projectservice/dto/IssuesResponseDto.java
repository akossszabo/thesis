package com.thesis.projectservice.dto;

import java.util.List;

public class IssuesResponseDto extends BaseResponse {
	
	private List<IssueDto> items;

	public List<IssueDto> getItems() {
		return items;
	}

	public void setItems(List<IssueDto> items) {
		this.items = items;
	}
}
