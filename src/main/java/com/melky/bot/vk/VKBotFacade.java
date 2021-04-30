package com.melky.bot.vk;

import com.google.gson.JsonObject;
import com.melky.bot.vk.handlers.CallbackAPIHandler;
import org.springframework.stereotype.Component;

@Component
public class VKBotFacade {

    private CallbackAPIHandler callbackAPIHandler;

    public VKBotFacade(CallbackAPIHandler callbackAPIHandler) {
        this.callbackAPIHandler = callbackAPIHandler;
    }

    public void handMessage(JsonObject json){
        callbackAPIHandler.parse(json);
    }
}
