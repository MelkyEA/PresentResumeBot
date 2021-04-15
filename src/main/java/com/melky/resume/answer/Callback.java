package com.melky.resume.answer;

import com.melky.resume.Service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class Callback {

    private ReplyMessageService replyMessageService;

    public Callback(ReplyMessageService replyMessageService) {
        this.replyMessageService = replyMessageService;
    }

    public SendMessage getAnswerProjects(long chatId){
        SendMessage sendMessage = replyMessageService.getReplyMessage(chatId, "reply.Callback");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton callbackButton = new InlineKeyboardButton();
        callbackButton.setCallbackData("callback");
        callbackButton.setText("Резюме");
        callbackButton.setUrl("https://hh.ru/resume/3e49e905ff07e9392e0039ed1f30434d393148");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(callbackButton);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
}
