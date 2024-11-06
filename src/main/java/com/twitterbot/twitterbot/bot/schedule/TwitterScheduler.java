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
            "Gemini...Nah, you cant do that",
            "Libra, you are going to break your smartphone today",
            "Baggy is not for you Sagittarius,",
    };
    private static final Random RANDOM = new Random();

    public static String generateUniquePrompt() {
        String example = EXAMPLES[RANDOM.nextInt(EXAMPLES.length)];
        String zodiacSign = ZODIAC_SIGNS[RANDOM.nextInt(ZODIAC_SIGNS.length)];

        return String.format(
                "Generate a sentence about the zodiac sign %s in 9 words. " +
                "Example: %s. It should be funny and sarcastic " +
                "and should be like something that happens to many people." +
                "Don't forget to mention the zodiac sign, %s.",
                zodiacSign, example, zodiacSign
        );
    }

    @Scheduled(fixedRate = 10*60*1000)
//    @Scheduled(cron = "0 25 * * * ?")
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
