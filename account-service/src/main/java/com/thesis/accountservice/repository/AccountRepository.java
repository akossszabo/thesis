package com.thesis.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thesis.accountservice.domain.Account;

public interface AccountRepository extends JpaRepository<Account,Long>{

	Account findByEmail(String email);

	public Account findByUsername(String username);

}
