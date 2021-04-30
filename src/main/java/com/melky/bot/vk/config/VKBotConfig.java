package com.melky.bot.vk.config;

import com.melky.bot.vk.VKBot;
import com.melky.bot.vk.VKBotFacade;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "vk")
public class VKBotConfig {

    private String botToken;
    private String groupId;

    @Bean
    public VKBot vkBot(VKBotFacade vkBotFacade){
        VKBot vkBot = new VKBot(vkBotFacade);
        vkBot.setVKToken(botToken);
        vkBot.setGroupID(Integer.parseInt(groupId));
        vkBot.setRequestAnswer("ok");
        return vkBot;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
