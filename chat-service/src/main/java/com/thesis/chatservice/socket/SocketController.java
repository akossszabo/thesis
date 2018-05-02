package com.thesis.chatservice.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.thesis.chatservice.dto.MessageDto;
import com.thesis.chatservice.service.MessageService;

@Controller
@CrossOrigin(origins = "http://localhost:8080")
public class SocketController {

	@Autowired
	private MessageService msgService;

	@MessageMapping("/message/{projectId}")
	@SendTo("/topic/{projectId}")
	public MessageDto chat(@DestinationVariable String projectId, MessageDto message) throws Exception {

		msgService.saveMessage(message, projectId);
		return message;
	}
}
