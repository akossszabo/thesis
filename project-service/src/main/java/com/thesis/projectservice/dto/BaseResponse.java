package com.thesis.projectservice.dto;

import org.springframework.http.HttpStatus;

public class BaseResponse {
	private String message;

	public BaseResponse() {
	}

	public BaseResponse(HttpStatus status, String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}