package com.thesis.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thesis.projectservice.domain.Project;

public interface ProjectRepository  extends JpaRepository<Project,Long>{

}
