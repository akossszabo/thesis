package com.thesis.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thesis.projectservice.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long>{


}
