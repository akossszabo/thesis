package com.thesis.dataservice.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thesis.dataservice.domain.Account;

public interface AccountRepository extends JpaRepository<Account,Long>{

}
