package com.melky.bot.vk.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.melky.bot.vk.VKBot;
import com.melky.bot.vk.VKBotState;
import com.melky.bot.vk.model.User;
import com.melky.bot.vk.repos.UserRepo;
import com.melky.bot.weather.Weather;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public class CallbackAPIHandler {

    private VKBot vkBot;
    private Weather weather;
    @Value("${vk.confCode}")
    private String confirmationCode;
    @Autowired
    private UserRepo userRepo;

    private final Gson gson = new Gson();
    private TransportClient transportClient;
    private VkApiClient vk;
    private GroupActor groupActor;

    public CallbackAPIHandler(@Lazy VKBot vkBot, Weather weather) {
        this.vkBot = vkBot;
        this.weather = weather;
        transportClient = new HttpTransportClient();
        vk = new VkApiClient(transportClient);
        groupActor = new GroupActor(vkBot.getGroupID(), vkBot.getVKToken());
    }

    public String parse (JsonObject json) {
        String type = json.get("type").getAsString();
        String response = "";
        switch (type) {
            case "message_new":
                String text = json.getAsJsonObject("object").get("body").getAsString();
                long userId = json.getAsJsonObject("object").get("user_id").getAsLong();
                //Ищем пользователя в DB
                Optional<User> resultFromDB = userRepo.findById(userId);
                ArrayList<User> arrUsers = new ArrayList<>();
                resultFromDB.ifPresent(arrUsers::add);
                User userFromDB = null;
                //Если в DB нет такого пользователя, создаем нового и добавляем в DB
                if(arrUsers.isEmpty()){
                    User user = new User(userId, VKBotState.SHOW_MAIN_MENU);
                    userRepo.save(user);
                    userFromDB = user;
                }else{
                    userFromDB = arrUsers.get(0);
                }

                setState(text, userFromDB);
                response = "ok";
            break;
            //Отсылаем confirmation код
            case "confirmation":
                response = getConfirmationCode();
            break;
        }
        return response;
    }

    // Опредялем состояние бота по сообщению
    private void setState(String text, User user){
        switch (text.toLowerCase()) {
            case "начать":
                user.setVkBotState(VKBotState.START);
                break;
            case "выбрать другой город":
                user.setCity(null);
            case "узнать погоду":
                user.setVkBotState(user.getCity() == null ? VKBotState.CHANGE_CITY : VKBotState.SHOW_CURRENT_WEATHER);
                break;
            case "узнать список дел":
                user.setVkBotState(VKBotState.SHOW_TODO_LIST);
                break;
            case "создать новый список дел":
                user.setVkBotState(VKBotState.CREATE_TODO_LIST);
                break;
            case "москва":
                user.setCity("Moscow");
                user.setVkBotState(VKBotState.SHOW_CURRENT_WEATHER);
                break;
            case "санкт-петербург":
                user.setCity("Saint Petersburg");
                user.setVkBotState(VKBotState.SHOW_CURRENT_WEATHER);
                break;
            case "казань":
                user.setCity("Kazan");
                user.setVkBotState(VKBotState.SHOW_CURRENT_WEATHER);
                break;
            case "екатеринбург":
                user.setCity("Yekaterinburg");
                user.setVkBotState(VKBotState.SHOW_CURRENT_WEATHER);
                break;
            case "омск":
                user.setCity("Omsk");
                user.setVkBotState(VKBotState.SHOW_CURRENT_WEATHER);
                break;
            case "пенза":
                user.setCity("Penza");
                user.setVkBotState(VKBotState.SHOW_CURRENT_WEATHER);
                break;
            case "жуковский":
                user.setCity("Zhukovskiy");
                user.setVkBotState(VKBotState.SHOW_CURRENT_WEATHER);
                break;
            case "самара":
                user.setCity("Samara");
                user.setVkBotState(VKBotState.SHOW_CURRENT_WEATHER);
                break;
            case "уфа":
                user.setCity("Ufa");
                user.setVkBotState(VKBotState.SHOW_CURRENT_WEATHER);
                break;
            case "вернуться в главное меню":
                user.setVkBotState(VKBotState.SHOW_MAIN_MENU);
                break;
            default:
                user.setVkBotState(VKBotState.UNKNOWN_MESSAGE);


        }
        messageNew(text, user);
    }

    // Отсылаем сообение в зависимости от состояния
    private void messageNew(String text, User user) {
        Random random = new Random();
        String reply = "";
        Keyboard keyboard = getMainKeyboard();
        VKBotState state = user.getVkBotState();
        long userId = user.getUserId();

        if(state.equals(VKBotState.START)){
            reply = "Доброго времени суток. Этот бот сделал, как пет-проект, который может показать погоду" +
                    " или показать составленный вами список дел. Автор @lexamelky";
            user.setVkBotState(VKBotState.SHOW_MAIN_MENU);
        }
        if(state.equals(VKBotState.CHANGE_CITY)){
            reply = "Выберите город из списка ниже";
            keyboard = getCityKeyboard();
            user.setVkBotState(VKBotState.SHOW_CURRENT_WEATHER);
        }
        if(state.equals(VKBotState.SHOW_CURRENT_WEATHER)){
            reply = weather.getCurrentWeather(user.getCity());
            keyboard = getWeatherKeyboard();
            user.setVkBotState(VKBotState.SHOW_MAIN_MENU);
        }
        if(state.equals(VKBotState.SHOW_TODO_LIST)){
            reply = "Данная функция пока не реализована";
            user.setVkBotState(VKBotState.SHOW_MAIN_MENU);
        }
        if(state.equals(VKBotState.CREATE_TODO_LIST)){
            reply = "Данная функция пока не реализована";
            user.setVkBotState(VKBotState.SHOW_MAIN_MENU);
        }
        if(state.equals(VKBotState.UNKNOWN_MESSAGE)){
            reply = "Я вас не понял, выберите кнопку ниже";
            user.setVkBotState(VKBotState.SHOW_MAIN_MENU);
        }
        if(state.equals(VKBotState.SHOW_MAIN_MENU)){
            reply = "Выберите раздел ниже";
            user.setVkBotState(VKBotState.SHOW_MAIN_MENU);
        }



        try {
            vk.messages().send(groupActor).message(reply).userId((int)userId)
                    .randomId(random.nextInt(10000)).keyboard(keyboard).execute();
        }catch (ApiException | ClientException e){
            e.printStackTrace();
        }
        userRepo.save(user);


    }

    // Клавиатура главного меню
    private Keyboard getMainKeyboard() {
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

    //Клавиатура выбора городов
    private Keyboard getCityKeyboard() {
        Keyboard keyboard = new Keyboard();
        List<List<KeyboardButton>> allKey = new ArrayList<>();
        List<KeyboardButton> line1 = new ArrayList<>();
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Москва").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Санкт-петербург").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Казань").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
        allKey.add(line1);
        List<KeyboardButton> line2 = new ArrayList<>();
        line2.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Екатеринбруг").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
        line2.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Омск").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
        line2.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Пенза").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
        allKey.add(line2);
        List<KeyboardButton> line3 = new ArrayList<>();
        line3.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Жуковский").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
        line3.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Самара").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
        line3.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Уфа").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
        allKey.add(line3);
        keyboard.setButtons(allKey);
        return keyboard;
    }

    //Клавиатура после получения погоды
    private Keyboard getWeatherKeyboard() {
        Keyboard keyboard = new Keyboard();
        List<List<KeyboardButton>> allKey = new ArrayList<>();
        List<KeyboardButton> line1 = new ArrayList<>();
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Выбрать другой город").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.PRIMARY));
        allKey.add(line1);
        List<KeyboardButton> line2 = new ArrayList<>();
        line2.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Вернуться в главное меню").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
        allKey.add(line2);
        keyboard.setButtons(allKey);
        return keyboard;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
}
