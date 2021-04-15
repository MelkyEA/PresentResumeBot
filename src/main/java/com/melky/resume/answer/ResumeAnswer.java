package com.melky.resume.answer;

import com.melky.resume.Service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResumeAnswer {

    private ReplyMessageService replyMessageService;

    public ResumeAnswer(ReplyMessageService replyMessageService) {
        this.replyMessageService = replyMessageService;
    }

    public SendMessage getAnswerResume(long chatId){
        SendMessage sendMessage = replyMessageService.getReplyMessage(chatId, "reply.Resume");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton hhButton = new InlineKeyboardButton();
        hhButton.setCallbackData("resume");
        hhButton.setText("Резюме на hh.ru");
        hhButton.setUrl("https://hh.ru/resume/3e49e905ff07e9392e0039ed1f30434d393148");
        InlineKeyboardButton uploadResume = new InlineKeyboardButton();
        uploadResume.setCallbackData("uploadResume");
        uploadResume.setText("Скачать файл резюме.");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(hhButton);
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(uploadResume);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
}
