package com.thesis.accountservice.dto;

import java.util.List;

public class DeleteAccountsRequestDto {
	private List<Long> accountIds;

	public List<Long> getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(List<Long> accountIds) {
		this.accountIds = accountIds;
	}
	
}
