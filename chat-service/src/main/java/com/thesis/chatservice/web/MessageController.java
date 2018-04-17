package com.thesis.chatservice.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.thesis.chatservice.dto.MessageDto;

@RestController
public class MessageController {

	@Autowired
	private MessageService msgService;
	
	public List<MessageDto> getMessageByProject() {
		return null;
	}
}
