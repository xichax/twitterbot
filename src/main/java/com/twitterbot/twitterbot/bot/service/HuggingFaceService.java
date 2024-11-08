package com.twitterbot.twitterbot.bot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class HuggingFaceService {

    @Value("${spring.ai.huggingface.chat.api-key}")
    private String huggingApiToken;

    @Value("${spring.ai.huggingface.chat.url}")
    private String huggingBasePath;

    @Autowired
    private WebClient webClient;

    private static final List<String> MODELS = List.of(
            "mistralai/Mistral-7B-Instruct-v0.2",
            "mistralai/Mixtral-8x7B-Instruct-v0.1"
//            "meta-llama/Llama-3.2-1B-Instruct"
    );

    private static final Random RANDOM = new Random();

    public Mono<String> getHuggingResponse(String message) {
        List<Map<String, String>> messages = List.of(
                Map.of("role", "user", "content", message)
        );

        String selectedModel = MODELS.get(RANDOM.nextInt(MODELS.size()));

        Map<String, Object> payload = Map.of(
                "model", selectedModel,
                "messages", messages,
                "max_tokens", 500,
                "stream", false
        );

        return webClient.post()
                .uri(selectedModel + "/v1/chat/completions")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractContentFromResponse)
                .doOnSuccess(response -> System.out.println("Final response: " + response))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                        new RuntimeException("Failed after multiple retries", retrySignal.failure())));
    }

    private String extractContentFromResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode choicesNode = jsonNode.path("choices");
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                String content = choicesNode.get(0).path("message").path("content").asText("");
                return content.replaceAll("\\(.*?\\)", "").trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
