package com.melky.resume.config;

import com.melky.resume.Bot;
import com.melky.resume.BotFacade;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {

    private String userName;
    private String botToken;
    private String webhookPath;

    @Bean
    public Bot bot(BotFacade botFacade){
        Bot bot = new Bot(botFacade);
        bot.setBotName(userName);
        bot.setBotToken(botToken);
        bot.setWebhookPath(webhookPath);
        return bot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getWebhookPath() {
        return webhookPath;
    }

    public void setWebhookPath(String webhookPath) {
        this.webhookPath = webhookPath;
    }
}
