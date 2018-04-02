package com.thesis.chatservice.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class SocketController {

	@Autowired
	private SimpMessagingTemplate template;

	@MessageMapping("/message/{projectId}")
	@SendTo("/topic/{projectId}")
	public String chat(@DestinationVariable String projectId, String message) throws Exception {
		System.out.println(message + projectId);
		return "hello, " + message+ " " + projectId;
	}
}
