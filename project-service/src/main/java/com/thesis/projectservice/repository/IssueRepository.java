package com.thesis.projectservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thesis.projectservice.domain.Issue;

public  interface IssueRepository extends JpaRepository<Issue,Long>{


	List<Issue> findByOrderByCreationDate();

}
