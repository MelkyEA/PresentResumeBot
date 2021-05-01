package com.melky.bot.vk.model;

import com.melky.bot.vk.VKBotState;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private Long userId;
    private VKBotState vkBotState;
    private String city;

    public User() {
    }

    public User(Long userId, VKBotState vkBotState) {
        this.userId = userId;
        this.vkBotState = vkBotState;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public VKBotState getVkBotState() {
        return vkBotState;
    }

    public void setVkBotState(VKBotState vkBotState) {
        this.vkBotState = vkBotState;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
