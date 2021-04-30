package com.melky.bot.telegram.answer;

import com.melky.bot.telegram.Service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectsAnswer {
    private ReplyMessageService replyMessageService;

    public ProjectsAnswer(ReplyMessageService replyMessageService) {
        this.replyMessageService = replyMessageService;
    }

    public SendMessage getAnswerProjects(long chatId){
        SendMessage sendMessage = replyMessageService.getReplyMessage(chatId, "reply.Projects");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton gitButton = new InlineKeyboardButton();
        gitButton.setCallbackData("projects");
        gitButton.setText("GitHub");
        gitButton.setUrl("https://github.com/MelkyEA");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(gitButton);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return sendMessage;
    }
}
