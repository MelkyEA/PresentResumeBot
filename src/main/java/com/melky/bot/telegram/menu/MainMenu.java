package com.melky.bot.telegram.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainMenu {
    public SendMessage getMainMenuMessage(final long chatId){
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();
        final SendMessage mainMenuMessage = createMessageWithKeyboard(chatId, replyKeyboardMarkup);

        return mainMenuMessage;
    }

    private ReplyKeyboardMarkup getMainMenuKeyboard(){

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow  myExp = new KeyboardRow();
        myExp.add(new KeyboardButton("Мой опыт"));
        keyboardRows.add(myExp);
        KeyboardRow myProjects = new KeyboardRow();
        myProjects.add(new KeyboardButton("Мои проекты"));
        keyboardRows.add(myProjects);
        KeyboardRow callback = new KeyboardRow();
        keyboardRows.add(callback);
        callback.add(new KeyboardButton("Связь со мной"));

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;

    }

    private SendMessage createMessageWithKeyboard(final long chatId,
                                                  final ReplyKeyboardMarkup replyKeyboardMarkup){
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Здравствуйте, меня зовут <b>Алексей Малышев</b>. Здесь представлена презентация моего резюме " +
                "на вакансию Junior Java Developer с помощью Telegram API + SPRING. " +
                "Для того, чтобы узнать обо мне побольше, воспользуйтесь кнопками ниже.");
        if(replyKeyboardMarkup != null){
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }
}
