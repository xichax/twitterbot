package com.twitterbot.twitterbot.bot.controller;

import com.twitterbot.twitterbot.bot.service.HuggingFaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TwitterBotController {

    @Autowired
    private HuggingFaceService huggingFaceService;

    @GetMapping("/chat")
    public Mono<String> chat(@RequestParam String message) {
        return huggingFaceService.getHuggingResponse(message);
    }

    @GetMapping("/hit")
    public String hitMe() {
        return "got hit";
    }
}
