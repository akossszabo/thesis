package com.thesis.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thesis.projectservice.domain.Issue;

public  interface IssueRepository extends JpaRepository<Issue,Long>{

}
