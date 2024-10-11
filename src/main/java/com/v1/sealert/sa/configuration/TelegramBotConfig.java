package com.v1.sealert.sa.configuration;


import com.v1.sealert.sa.service.TelegramLongPollingBotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfig {

    @Bean
    public TelegramLongPollingBotService telegramLongPollingBotService(@Value("${spring.telegram.bot-name}") String botName,
                                                                       @Value("${spring.telegram.token}") String token)
                                                                                                throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        TelegramLongPollingBotService botService = new TelegramLongPollingBotService(botName, token);
        botService.registerBotCommands();
        botsApi.registerBot(botService);
        return botService;
    }
}
