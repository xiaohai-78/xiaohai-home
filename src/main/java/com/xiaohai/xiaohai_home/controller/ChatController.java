package com.xiaohai.xiaohai_home.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author xiaoyuntao
 * @date 2025/06/04
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

	private final ChatClient chatClient;

	private final CompiledGraph compiledGraph;

	private final ToolCallbackProvider toolCallbackProvider;

	@Autowired
	public ChatController(ChatClient chatClient, @Qualifier("simpleGraph") StateGraph stateGraph, ToolCallbackProvider toolCallbackProvider) throws GraphStateException {
		this.chatClient = chatClient;
		this.compiledGraph = stateGraph.compile();
		this.toolCallbackProvider = toolCallbackProvider;
	}

	@GetMapping(value = "/expand")
	public Map<String, Object> expand(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？", required = false) String query,
									  @RequestParam(value = "expandernumber", defaultValue = "3", required = false) Integer  expanderNumber,
									  @RequestParam(value = "threadid", defaultValue = "yingzi", required = false) String threadId) throws GraphRunnerException {
		RunnableConfig runnableConfig = RunnableConfig.builder().threadId(threadId).build();
		Map<String, Object> objectMap = new HashMap<>();
		objectMap.put("query", query);
		objectMap.put("expandernumber", expanderNumber);
		Optional<OverAllState> invoke = this.compiledGraph.invoke(objectMap, runnableConfig);
		return invoke.map(OverAllState::data).orElse(new HashMap<>());
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

	@PostMapping(value = "/mcp", produces = MediaType.APPLICATION_JSON_VALUE)
	public String mcp(@RequestBody Map<String, Object> message) {
		logger.info("Received chat request: {}", message);
		return chatClient.prompt((String) message.get("message"))
				.options(DashScopeChatOptions.builder()
						.withModel((String) message.get("qwen-plus"))
						.withToolCallbacks(Arrays.asList(toolCallbackProvider.getToolCallbacks()))
						.build())
				.call()
				.content();
	}
}
