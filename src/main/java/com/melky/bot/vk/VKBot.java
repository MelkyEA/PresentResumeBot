package com.melky.bot.vk;

import com.google.gson.JsonObject;
import com.melky.bot.vk.handlers.CallbackAPIHandler;



public class VKBot {

    private String VKToken;
    private int GroupID;
    private String requestAnswer;

    private VKBotFacade vkBotFacade;

    public VKBot(VKBotFacade vkBotFacade) {
        this.vkBotFacade = vkBotFacade;
    }

    public void receivedMessage(JsonObject json){
        vkBotFacade.handMessage(json);
    }

    public String getVKToken() {
        return VKToken;
    }

    public void setVKToken(String VKToken) {
        this.VKToken = VKToken;
    }

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int groupID) {
        GroupID = groupID;
    }

    public String getRequestAnswer() {
        return requestAnswer;
    }

    public void setRequestAnswer(String requestAnswer) {
        this.requestAnswer = requestAnswer;
    }
}
