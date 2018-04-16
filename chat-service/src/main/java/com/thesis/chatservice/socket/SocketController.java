package com.thesis.chatservice.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.thesis.chatservice.dto.MessageDto;

@Controller
@CrossOrigin(origins = "http://localhost:8080")
public class SocketController {

	@Autowired
	private SimpMessagingTemplate template;

	@MessageMapping("/message/{projectId}")
	@SendTo("/topic/{projectId}")
	public String chat(@DestinationVariable String projectId, MessageDto message) throws Exception {

		System.out.println(message);
		return "hello, " + message + " " + projectId;
	}
}
