package com.thesis.chatservice.socket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SocketController {

	@Autowired
	private SimpMessagingTemplate template;

	@MessageMapping("/message/{project_id}")
	@SendTo("/topic/{project_id}")
	public String chat(@DestinationVariable String projectId, String message) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("src//main//resources//test-socket.pdf", "test-socket.pdf");
		System.out.println(message);
		return "hello, " + message;
	}
}
