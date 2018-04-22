package com.thesis.chatservice.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.thesis.chatservice.domain.ChatMessage;
import com.thesis.chatservice.dto.MessageDto;
import com.thesis.chatservice.service.ChatMessageRepo;

@Controller
@CrossOrigin(origins = "http://localhost:8080")
public class SocketController {

	@Autowired
	private ChatMessageRepo chatRepo;

	@MessageMapping("/message/{projectId}")
	@SendTo("/topic/{projectId}")
	public MessageDto chat(@DestinationVariable String projectId, MessageDto message) throws Exception {

		System.out.println(message);
		saveMessage(message, projectId);
		return message;
	}

	private void saveMessage(MessageDto message, String projectId) {
		ChatMessage msg = new ChatMessage();
		msg.setMessage(message.getText());
		msg.setProjectId(projectId);
		msg.setUsername(message.getUsername());
		msg.setSendDate(message.getTime());
		chatRepo.saveAndFlush(msg);
	}
}
