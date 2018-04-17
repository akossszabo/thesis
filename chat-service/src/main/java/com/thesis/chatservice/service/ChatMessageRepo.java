package com.thesis.chatservice.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thesis.chatservice.domain.ChatMessage;

public interface ChatMessageRepo extends JpaRepository<ChatMessage,Long>{

}
