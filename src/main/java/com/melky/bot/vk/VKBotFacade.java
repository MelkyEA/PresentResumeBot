package com.melky.bot.vk;

import com.google.gson.JsonObject;
import com.melky.bot.vk.handlers.CallbackAPIHandler;
import org.springframework.stereotype.Component;

@Component
public class VKBotFacade {

    private CallbackAPIHandler callbackAPIHandler;

    private String requestAnswer;

    public VKBotFacade(CallbackAPIHandler callbackAPIHandler) {
        this.callbackAPIHandler = callbackAPIHandler;
    }

    public void handMessage(JsonObject json){
       requestAnswer = callbackAPIHandler.parse(json);
    }

    public CallbackAPIHandler getCallbackAPIHandler() {
        return callbackAPIHandler;
    }

    public void setCallbackAPIHandler(CallbackAPIHandler callbackAPIHandler) {
        this.callbackAPIHandler = callbackAPIHandler;
    }

    public String getRequestAnswer() {
        return requestAnswer;
    }

    public void setRequestAnswer(String requestAnswer) {
        this.requestAnswer = requestAnswer;
    }
}
