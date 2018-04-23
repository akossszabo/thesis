package com.thesis.chatservice.socket;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class HttpSessionIdHandshakeInterceptor implements HandshakeInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(HttpSessionIdHandshakeInterceptor.class);

	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {

	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		request.getHeaders().add("Connection", "upgrade");
		request.getHeaders().add("Upgrade", "websocket");
		logger.info("Handshake happened, headers: " + request.getHeaders());
		response.getHeaders().add("Connection", "Upgrade");
		response.getHeaders().add("Upgrade", "WebSocket");
		logger.info("response headers: " + response.getHeaders());
		return true;
	}
}