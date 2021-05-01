package com.melky.bot.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.melky.bot.telegram.Bot;
import com.melky.bot.vk.VKBot;
import com.melky.bot.vk.VKBotFacade;
import com.melky.bot.vk.handlers.CallbackAPIHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebHookController {

    private final Bot bot;
    private VKBotFacade vkBotFacade;
    private final Gson gson = new Gson();

    public WebHookController(Bot bot, VKBotFacade vkBotFacade) {
        this.bot = bot;
        this.vkBotFacade = vkBotFacade;
    }

    @RequestMapping(value = "/telegram", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){
        return bot.onWebhookUpdateReceived(update);
    }

    @RequestMapping(value = "/vk", method = RequestMethod.POST)
    public String callbackReceived(@RequestBody String body){
        JsonObject jsonObject =(JsonObject)this.gson.fromJson(body, JsonObject.class);
       vkBotFacade.handMessage(jsonObject);
        return vkBotFacade.getRequestAnswer();
    }
}