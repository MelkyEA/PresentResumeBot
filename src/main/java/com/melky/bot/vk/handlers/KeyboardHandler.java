package com.melky.bot.vk.handlers;

import com.melky.bot.vk.model.ElementToDoList;
import com.vk.api.sdk.objects.messages.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardHandler {


    // Клавиатура главного меню
    Keyboard getMainKeyboard() {
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
    Keyboard getCityKeyboard() {
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
    Keyboard getWeatherKeyboard() {
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

    //Клавиатура для взаимодействия со списком
    Keyboard getShowListKeyboard() {
        Keyboard keyboard = new Keyboard();
        List<List<KeyboardButton>> allKey = new ArrayList<>();
        List<KeyboardButton> line1 = new ArrayList<>();
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Редактировать список").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.PRIMARY));
        allKey.add(line1);
        List<KeyboardButton> line2 = new ArrayList<>();
        line2.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Удалить список").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.NEGATIVE));
        allKey.add(line2);
        List<KeyboardButton> line3 = new ArrayList<>();
        line3.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Вернуться в главное меню").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
        allKey.add(line3);
        keyboard.setButtons(allKey);
        return keyboard;
    }

    //Клавиатура выбора определнного элемента списка
    Keyboard getChangeElementKeyboard(ArrayList<ElementToDoList> list) {
        Keyboard keyboard = new Keyboard();
        List<List<KeyboardButton>> allKey = new ArrayList<>();
        List<KeyboardButton> line1 = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            if(i < 5){
                line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel(String.valueOf(i + 1)).setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
            }
        }
        allKey.add(line1);
        if(list.size() > 4) {
            List<KeyboardButton> line2 = new ArrayList<>();
            for (int i = 5; i < list.size(); i++) {
                line2.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel(String.valueOf(i + 1)).setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
            }

            allKey.add(line2);
        }
        List<KeyboardButton> line3 = new ArrayList<>();
        line3.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Закончить редактирование").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.NEGATIVE));
        allKey.add(line3);
        keyboard.setButtons(allKey);
        return keyboard;
    }

    //Клавиатура для редактирования определнного элемента
    Keyboard getEditCurrentElementKeyboard() {
        Keyboard keyboard = new Keyboard();
        List<List<KeyboardButton>> allKey = new ArrayList<>();
        List<KeyboardButton> line1 = new ArrayList<>();
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Изменить текст ").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.PRIMARY));
        allKey.add(line1);
        List<KeyboardButton> line2 = new ArrayList<>();
        line2.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Изменить выполненность ").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.DEFAULT));
        allKey.add(line2);
        keyboard.setButtons(allKey);
        List<KeyboardButton> line3 = new ArrayList<>();
        line3.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Отменить").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.NEGATIVE));
        allKey.add(line3);
        keyboard.setButtons(allKey);
        return keyboard;
    }
}
