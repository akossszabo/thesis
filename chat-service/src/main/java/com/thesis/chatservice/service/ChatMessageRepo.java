package com.thesis.chatservice.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thesis.chatservice.domain.ChatMessage;

public interface ChatMessageRepo extends JpaRepository<ChatMessage,Long>{

	public List<ChatMessage> findByProjectIdOrderBySendDate(String projectId);

}
