package com.thesis.chatservice.web;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.thesis.chatservice.dto.MessageDto;

@RestController
public class MessageController {

	private static Logger log = LoggerFactory.getLogger(MessageController.class);

	@Autowired
	private MessageService msgService;

	@GetMapping("/messages/{projectId}")
	public List<MessageDto> getMessageByProject(@PathVariable String projectId) {
		List<MessageDto> messages = new ArrayList<>();
		try {
			messages = msgService.getFormerMessagesByProject(projectId);
		} catch (Throwable t) {
			messages = new ArrayList<>();
			log.error("Error while listing former messages, ", t);

		}
		return messages;
	}
}
