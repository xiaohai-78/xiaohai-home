package com.xiaohai.xiaohai_home.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * @author xiaoyuntao
 * @date 2025/06/04
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

	private final ChatClient chatClient;

	@Autowired
	public ChatController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping(value = "/request", produces = MediaType.APPLICATION_JSON_VALUE)
	public String chatRequest(@RequestBody Map<String, Object> message) {
		logger.info("Received chat request: {}", message);
		return chatClient.prompt((String) message.get("message"))
			.options(DashScopeChatOptions.builder().withModel((String) message.get("model")).build())
			.call()
			.content();
	}

	@PostMapping(value = "/request/stream", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<String> chatRequestStream(@RequestBody Map<String, Object> message) {
		logger.info("Received chat request: {}", message);
		return chatClient.prompt((String) message.get("message"))
				.options(DashScopeChatOptions.builder().withModel((String) message.get("model")).build())
				.stream().content();
	}

	@PostMapping(value = "/tool", produces = MediaType.APPLICATION_JSON_VALUE)
	public String tool(@RequestBody Map<String, Object> message) {
		logger.info("Received chat request: {}", message);
		return chatClient.prompt((String) message.get("message"))
				.options(DashScopeChatOptions.builder()
						.withModel((String) message.get("model"))
						.withToolName("baiduSearch")
						.build())
				.call()
				.content();
	}
}
