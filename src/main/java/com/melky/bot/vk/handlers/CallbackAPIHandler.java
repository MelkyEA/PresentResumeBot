package com.melky.bot.vk.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.melky.bot.vk.VKBot;
import com.vk.api.sdk.callback.CallbackApi;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.callback.messages.CallbackMessage;
import com.vk.api.sdk.objects.messages.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class CallbackAPIHandler {

    private VKBot vkBot;
    private final Gson gson = new Gson();

    public CallbackAPIHandler(@Lazy VKBot vkBot) {
        this.vkBot = vkBot;

    }

    public void parse (JsonObject json){
        String type = json.get("type").getAsString();

        switch (type){
            case "message_new":
                String text = json.getAsJsonObject("object").get("body").getAsString();
                int userId = json.getAsJsonObject("object").get("user_id").getAsInt();
                messageNew(text, userId);
                break;
            case "confirmation":
                confirmation();
        }
    }


    public void messageNew(String text, int userId) {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        GroupActor groupActor = new GroupActor(vkBot.getGroupID(), vkBot.getVKToken());
        Random random = new Random();
        vkBot.setRequestAnswer("ok");
        String reply = "";
        switch (text.toLowerCase()) {
            case "начать":
                reply = "Привет! Данный бот используется для получения погоды в городе Москва, а также составления списка дел \n"
                        + "Автор @lexamelky";
                break;
            case "узнать погоду":
                reply = "Погода пока не реализована(";
                break;
            case "узнать список дел":
                reply = "Список дел не реализован(";
                break;
            case "создать новый список дел":
                reply = "Создание нового списка не реализовано(";
                break;
            case "usedtelegram":
                reply = "Кто-то воспользовался ботом в телеграм";
                break;
            default:
                reply = "Я вас не понял, воспользуйтесь кнопками ниже";
        }
        try {
            vk.messages().send(groupActor).message(reply).userId(userId)
                    .randomId(random.nextInt(10000)).keyboard(getKeyboard()).execute();
        }catch (ApiException | ClientException e){
            e.printStackTrace();
        }


    }


    public void confirmation() {
        vkBot.setRequestAnswer("55439aea");
    }

    private Keyboard getKeyboard() {
        Keyboard keyboard = new Keyboard();
        List<List<KeyboardButton>> allKey = new ArrayList<>();
        List<KeyboardButton> line1 = new ArrayList<>();
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Узнать погоду").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
        allKey.add(line1);
        List<KeyboardButton> line2 = new ArrayList<>();
        line2.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Узнать список дел").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
        allKey.add(line2);
        List<KeyboardButton> line3 = new ArrayList<>();
        line3.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Создать новый список дел").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        allKey.add(line3);
        keyboard.setButtons(allKey);
        return keyboard;
    }

}
