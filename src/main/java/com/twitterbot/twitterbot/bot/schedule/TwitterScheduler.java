package com.twitterbot.twitterbot.bot.schedule;

import com.twitterbot.twitterbot.bot.service.HuggingFaceService;
import com.twitterbot.twitterbot.bot.service.TwitterBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class TwitterScheduler {

    @Autowired
    TwitterBotService twitterBotService;

    @Autowired
    HuggingFaceService huggingFaceService;

    private static final String[] ZODIAC_SIGNS = {
            "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
            "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
    };
    private static final String[] EXAMPLES = {
            "...nah, you cant do that.",
            ", you are going to break your smartphone today.",
            ", baggy is not for you.",
    };
    private static final Random RANDOM = new Random();


    public static String generateUniquePrompt() {
        String example = EXAMPLES[RANDOM.nextInt(EXAMPLES.length)];
        String zodiacSign = ZODIAC_SIGNS[RANDOM.nextInt(ZODIAC_SIGNS.length)];

        return String.format(
                "Generate a funny and sarcastic 9-word sentence about the zodiac sign %s." +
                " The sentence should describe a relatable situation that happens to many people and must mention '%s'." +
                "' Example: Libra, %s.",
                zodiacSign, zodiacSign, example
        );
    }

    @Scheduled(cron = "0 45 * * * ?")
    public void tweetEveryHour() {
        String request = generateUniquePrompt();
        String response = huggingFaceService.getHuggingResponse(request).block();

        if (response != null && !response.trim().isEmpty()) {
            log.info("Generated quote: {}", response);
            twitterBotService.postTweet(response);
        } else {
            log.warn("Received an empty or null response from Hugging Face service.");
        }
    }
}
