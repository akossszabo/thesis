package com.thesis.projectservice.dto;

public class StatisticsDto extends BaseResponse {
	private long issueNumber;
	private long projectNumber;
	private long userNumber;
	private long commentNumber;
	
	public long getIssueNumber() {
		return issueNumber;
	}
	public void setIssueNumber(long issueNumber) {
		this.issueNumber = issueNumber;
	}
	public long getProjectNumber() {
		return projectNumber;
	}
	public void setProjectNumber(long projectNumber) {
		this.projectNumber = projectNumber;
	}
	public long getUserNumber() {
		return userNumber;
	}
	public void setUserNumber(long userNumber) {
		this.userNumber = userNumber;
	}
	public long getCommentNumber() {
		return commentNumber;
	}
	public void setCommentNumber(long commentNumber) {
		this.commentNumber = commentNumber;
	}
	
	
}
