package com.thesis.accountservice.dto;

import java.util.List;

public class RegistrationRequestDto {

	private List<AccountDto> accounts;

	public List<AccountDto> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<AccountDto> accounts) {
		this.accounts = accounts;
	}
}
