package com.thesis.projectservice.dto;

import java.util.List;

public class DeleteIssuesRequestDto {
	private List<Long> issueIds;

	public List<Long> getIssueIds() {
		return issueIds;
	}

	public void setIssueIds(List<Long> issueIds) {
		this.issueIds = issueIds;
	}

}
