package com.thesis.accountservice.dto;

import java.util.List;

public class AccountsToSelectDto extends BaseResponse {

	private List<AccountEmailNameDto> items;

	public List<AccountEmailNameDto> getItems() {
		return items;
	}

	public void setItems(List<AccountEmailNameDto> items) {
		this.items = items;
	}

}