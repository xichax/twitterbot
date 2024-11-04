package com.twitterbot.twitterbot.bot.service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class is for testing
 */
public class Chat {
    @Autowired
    private HuggingFaceService huggingFaceService;

    public void performChat() {
        String userMessage = "What is the capital of France?";
        huggingFaceService.getHuggingResponse(userMessage)
                .subscribe(response -> {
                    System.out.println("Response: " + response);
                }, error -> {
                    System.err.println("Error: " + error.getMessage());
                });
    }
}
