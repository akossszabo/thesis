package com.thesis.chatservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thesis.chatservice.domain.ChatMessage;
import com.thesis.chatservice.dto.MessageDto;

@Service
public class MessageService {

	@Autowired
	private ChatMessageRepo repo;

	public List<MessageDto> getFormerMessagesByProject(String projectId) {
		List<ChatMessage> messages = repo.findByProjectIdOrderBySendDate(projectId);
		return getMessageDtoFromMessages(messages);
	}

	private List<MessageDto> getMessageDtoFromMessages(List<ChatMessage> messages) {
		List<MessageDto> dtos = new ArrayList<>();
		for (ChatMessage m : messages) {
			MessageDto dto = new MessageDto();
			dto.setText(m.getMessage());
			dto.setUsername(m.getUsername());
			dto.setTime(m.getSendDate());
			dtos.add(dto);
		}
		return dtos;
	}

	public List<MessageDto> addMessageToProject(String projectId, MessageDto message) {
		ChatMessage m = new ChatMessage();
		m.setSendDate(message.getTime());
		m.setProjectId(projectId);
		m.setUsername(message.getUsername());
		m.setMessage(message.getText());
		repo.saveAndFlush(m);
		return new ArrayList<>();
	}

	public void saveMessage(MessageDto message, String projectId) {
		ChatMessage msg = new ChatMessage();
		msg.setMessage(message.getText());
		msg.setProjectId(projectId);
		msg.setUsername(message.getUsername());
		msg.setSendDate(message.getTime());
		repo.saveAndFlush(msg);
	}

}
