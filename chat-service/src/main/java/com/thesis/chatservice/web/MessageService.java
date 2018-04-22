package com.thesis.chatservice.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thesis.chatservice.domain.ChatMessage;
import com.thesis.chatservice.dto.MessageDto;
import com.thesis.chatservice.service.ChatMessageRepo;

@Service
public class MessageService {

	@Autowired
	private ChatMessageRepo repo;

	public List<MessageDto> getFormerMessagesByProject(String projectId) {
		List<ChatMessage> messages = repo.findByProjectIdOrderBySendDateDesc(projectId);
		return getMessageDtoFromMessages(messages);
	}

	private List<MessageDto> getMessageDtoFromMessages(List<ChatMessage> messages) {
		List<MessageDto> dtos = new ArrayList<>();
		for(ChatMessage m : messages){
			MessageDto dto = new MessageDto();
			dto.setText(m.getMessage());
			dto.setUsername(m.getUsername());
			dto.setTime(m.getSendDate());
			dtos.add(dto);
		}
		return dtos;
	}

}
