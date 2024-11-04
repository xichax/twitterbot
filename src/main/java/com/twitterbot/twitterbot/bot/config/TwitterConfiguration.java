package com.twitterbot.twitterbot.bot.config;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import org.springframework.ai.huggingface.HuggingfaceChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class TwitterConfiguration {


    @Value("${twitter.apiKey}")
    private String apiKey;

    @Value("${twitter.apiSecretKey}")
    private String apiSecretKey;

    @Value("${twitter.accessToken}")
    private String accessToken;

    @Value("${twitter.accessTokenSecret}")
    private String accessTokenSecret;

    @Value("${spring.ai.huggingface.chat.api-key}")
    private String huggingApiToken;

    @Value("${spring.ai.huggingface.chat.url}")
    private String huggingBasePath;

    @Bean
    public TwitterClient getTwitterClient () {
        return new TwitterClient(TwitterCredentials.builder()
                .apiKey(apiKey)
                .apiSecretKey(apiSecretKey)
                .accessToken(accessToken)
                .accessTokenSecret(accessTokenSecret)
                .build());
    }

    @Bean
    public HuggingfaceChatModel aiClient() {
        return new HuggingfaceChatModel(huggingApiToken, huggingBasePath);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(huggingBasePath)
                .defaultHeader("Authorization", "Bearer " + huggingApiToken)
                .build();
    }
}
