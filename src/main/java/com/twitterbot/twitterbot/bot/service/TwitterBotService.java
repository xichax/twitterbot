package com.twitterbot.twitterbot.bot.service;

import io.github.redouane59.twitter.TwitterClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwitterBotService {

    private final TwitterClient twitterClient;


    @Autowired
    public TwitterBotService(TwitterClient twitterClient) {
        this.twitterClient = twitterClient;
    }

    public void postTweet(String message) {
        try {
            twitterClient.postTweet(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
