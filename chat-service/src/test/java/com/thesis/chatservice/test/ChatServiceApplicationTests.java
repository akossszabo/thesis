package com.thesis.chatservice.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.thesis.chatservice.domain.ChatMessage;
import com.thesis.chatservice.dto.MessageDto;
import com.thesis.chatservice.service.ChatMessageRepo;
import com.thesis.chatservice.service.MessageService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatServiceApplicationTests {

	@Autowired
	private ChatMessageRepo repo;

	@Autowired
	private MessageService service;

	@Test
	public void saveMessagesTest() {
		String projectId = "1200";
		MessageDto dto = new MessageDto();
		dto.setText("test message for chat service");
		dto.setTime(new Date());
		dto.setUsername("Test User");
		service.saveMessage(dto, projectId);

		MessageDto dto2 = new MessageDto();
		dto2.setText("test message 2 for chat service");
		dto2.setTime(new Date());
		dto2.setUsername("Test User 2");
		service.saveMessage(dto2, projectId);

		List<ChatMessage> messages = repo.findByProjectIdOrderBySendDate(projectId);

		assertTrue(!messages.isEmpty());
		assertEquals(2, messages.size());
		assertEquals("test message for chat service", messages.get(0).getMessage());
		assertEquals("test message 2 for chat service", messages.get(1).getMessage());
		assertEquals("Test User", messages.get(0).getUsername());
		assertEquals("Test User 2", messages.get(1).getUsername());
		assertEquals(projectId, messages.get(0).getProjectId());
		assertEquals(projectId, messages.get(1).getProjectId());
		
		repo.deleteAll(messages);
	}

}
