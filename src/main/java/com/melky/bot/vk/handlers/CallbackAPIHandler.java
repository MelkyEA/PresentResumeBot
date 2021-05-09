package com.melky.bot.vk.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.melky.bot.vk.VKBot;
import com.melky.bot.vk.VKBotState;
import com.melky.bot.vk.model.ElementToDoList;
import com.melky.bot.vk.model.User;
import com.melky.bot.vk.repos.ElementToDoListRepo;
import com.melky.bot.vk.repos.UserRepo;
import com.melky.bot.weather.Weather;
import com.vdurmont.emoji.EmojiParser;
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

import java.util.*;

@Component
public class CallbackAPIHandler {

    private VKBot vkBot;
    private Weather weather;
    private KeyboardHandler keyboardHandler;
    @Value("${vk.confCode}")
    private String confirmationCode;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ElementToDoListRepo elementRepo;


    private final Gson gson = new Gson();
    private TransportClient transportClient;
    private VkApiClient vk;
    private GroupActor groupActor;

    public CallbackAPIHandler(@Lazy VKBot vkBot, Weather weather, KeyboardHandler keyboardHandler) {
        this.vkBot = vkBot;
        this.weather = weather;
        this.keyboardHandler = keyboardHandler;
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
        if(!user.getVkBotState().equals(VKBotState.CREATE_TODO_LIST) &&
                !user.getVkBotState().equals(VKBotState.EDIT_CURRENT_ELEMENT) &&
                !user.getVkBotState().equals(VKBotState.SAVE_TEXT_ELEMENT)  ||
                text.equalsIgnoreCase("конец") || text.equalsIgnoreCase("отменить") ||
                text.equalsIgnoreCase("закончить редактирование")) {
            switch (text.toLowerCase()) {
                case "начать":
                    user.setVkBotState(VKBotState.START);
                    break;
                case "выбрать другой город":
                    user.setCity(null);
                case "узнать погоду":
                    user.setVkBotState(user.getCity() == null ? VKBotState.CHANGE_CITY : VKBotState.SHOW_CURRENT_WEATHER);
                    break;
                case "отменить":
                case "закончить редактирование":
                case "конец":
                case "узнать список дел":
                    user.setVkBotState(VKBotState.SHOW_TODO_LIST);
                    break;
                case "создать новый список дел":
                    for(ElementToDoList e : user.getElementToDoListSet()){
                        elementRepo.deleteById(e.getId());
                    }
                    user.getElementToDoListSet().clear();
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
                case "удалить список":
                    for(ElementToDoList e : user.getElementToDoListSet()){
                        elementRepo.deleteById(e.getId());
                    }
                    user.getElementToDoListSet().clear();
                    user.setVkBotState(VKBotState.SHOW_MAIN_MENU);
                    break;
                case "редактировать список":
                    user.setVkBotState(VKBotState.EDIT_LIST);
                    break;
                case "изменить текст":
                    user.setVkBotState(VKBotState.EDIT_TEXT_ELEMENT);
                    break;
                case "изменить выполненность":
                    user.setVkBotState(VKBotState.EDIT_IS_SUCCESS_ELEMENT);
                    break;
                default:
                    user.setVkBotState(VKBotState.UNKNOWN_MESSAGE);


            }
        }
        messageNew(text, user);
    }

    // Отсылаем сообение в зависимости от состояния
    private void messageNew(String text, User user) {
        Random random = new Random();
        String reply = "";
        Keyboard keyboard = keyboardHandler.getMainKeyboard();
        VKBotState state = user.getVkBotState();
        long userId = user.getUserId();
        //Заполняем list пунктами из списка
        ArrayList<ElementToDoList> toDoLists = user.getToDoLists();


        if(state.equals(VKBotState.START)){
            reply = "Доброго времени суток. Этот бот сделан, как пет-проект, который может показать погоду" +
                    " или показать составленный вами список дел. Автор @lexamelky";
            user.setVkBotState(VKBotState.SHOW_MAIN_MENU);
        }
        if(state.equals(VKBotState.CHANGE_CITY)){
            reply = "Выберите город из списка ниже";
            keyboard = keyboardHandler.getCityKeyboard();
            user.setVkBotState(VKBotState.SHOW_CURRENT_WEATHER);
        }
        if(state.equals(VKBotState.SHOW_CURRENT_WEATHER)){
            reply = weather.getCurrentWeather(user.getCity());
            keyboard = keyboardHandler.getWeatherKeyboard();
            user.setVkBotState(VKBotState.SHOW_MAIN_MENU);
        }
        if(state.equals(VKBotState.SAVE_TEXT_ELEMENT)){
            ElementToDoList element = toDoLists.get(user.getEditElement());
            element.setText(text);
            elementRepo.save(element);
            reply = "Текст успешно изменен" + "\n";
            state = VKBotState.SHOW_TODO_LIST;
        }
        if(state.equals(VKBotState.EDIT_IS_SUCCESS_ELEMENT)){
            System.out.println(user.getEditElement());
            ElementToDoList element = toDoLists.get(user.getEditElement());
            element.setSuccess(!element.isSuccess());
            elementRepo.save(element);
            reply = "Выполненность успешно изменена";
            state = VKBotState.SHOW_TODO_LIST;
        }
        if(state.equals(VKBotState.SHOW_TODO_LIST)){

            if(toDoLists != null && !toDoLists.isEmpty()){
                for(int i = 0; i < toDoLists.size(); i++){
                  reply += i+1 + ". " + toDoLists.get(i).getText() + (toDoLists.get(i).isSuccess() ?
                          EmojiParser.parseToUnicode(":heavy_check_mark:") :
                          EmojiParser.parseToUnicode(":x:")) + "\n";
              }
              keyboard = keyboardHandler.getShowListKeyboard();
            } else {
                reply = "Твой список дел еще не создан. Для создания списка воспользуйся кнопкой ниже.";
            }
            user.setVkBotState(VKBotState.SHOW_MAIN_MENU);
        }
        if(state.equals(VKBotState.CREATE_TODO_LIST)){
            reply = "Отправь мне пункт, который ты хочешь добавить в список. Чтобы закончить добавление, " +
                    "введи слово \"Конец\" ";

            if(!text.equalsIgnoreCase("создать новый список дел"))
            user.getElementToDoListSet().add(new ElementToDoList(text, user));
            user.setVkBotState(VKBotState.CREATE_TODO_LIST);
        }
        if(state.equals(VKBotState.UNKNOWN_MESSAGE)){
            reply = "Я вас не понял, выберите кнопку ниже";
            user.setVkBotState(VKBotState.SHOW_MAIN_MENU);
        }
        if(state.equals(VKBotState.SHOW_MAIN_MENU)){
            reply = "Выберите раздел ниже";
            if(text.equalsIgnoreCase("удалить список")){
                reply = "Ваш список успешно удален";
            }
            user.setVkBotState(VKBotState.SHOW_MAIN_MENU);
        }
        if(state.equals(VKBotState.EDIT_LIST)){
            reply = "Выберите, какой пункт вы хотите отредактировать";
            keyboard = keyboardHandler.getChangeElementKeyboard(toDoLists);
            user.setVkBotState(VKBotState.EDIT_CURRENT_ELEMENT);
        }
        if(state.equals(VKBotState.EDIT_CURRENT_ELEMENT)){
            int el = Integer.parseInt(text) - 1;
            user.setEditElement(el);
            reply = "Пункт " + (el + 1) + ":\n" +
                    toDoLists.get(el).getText() + " " + (toDoLists.get(el).isSuccess() ?
                    EmojiParser.parseToUnicode(":heavy_check_mark:") :
                    EmojiParser.parseToUnicode(":x:")) + "\n" +
                    "Выбери снизу, что ты хочешь изменить";
            keyboard = keyboardHandler.getEditCurrentElementKeyboard();
            user.setVkBotState(VKBotState.SHOW_MAIN_MENU);
        }
        if(state.equals(VKBotState.EDIT_TEXT_ELEMENT)){
            reply = "Напишите текст, на который вы хотите поменять";
            user.setVkBotState(VKBotState.SAVE_TEXT_ELEMENT);
        }

        try {
            vk.messages().send(groupActor).message(reply).userId((int)userId)
                    .randomId(random.nextInt(10000)).keyboard(keyboard).execute();
        }catch (ApiException | ClientException e){
            e.printStackTrace();
        }
        userRepo.save(user);


    }



    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
}
