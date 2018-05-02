package com.thesis.projectservice.dto;

import java.util.List;

public class AccountsResponseDto extends BaseResponse {

	private List<AccountDto> items;

	public List<AccountDto> getItems() {
		return items;
	}

	public void setItems(List<AccountDto> items) {
		this.items = items;
	}

}
