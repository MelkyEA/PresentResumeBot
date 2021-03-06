package com.melky.bot.telegram.Service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class ReplyMessageService {

    private LocaleMessageService localeMessageService;


    public ReplyMessageService(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage){
        return new SendMessage(String.valueOf(chatId), localeMessageService.getMessage(replyMessage));
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage, int userId){
        return new SendMessage(String.valueOf(chatId),
                localeMessageService.getMessage(replyMessage)
        );
    }
    public SendMessage getReplyMessage(long chatId, String replyMessage, Object... args){
        return new SendMessage(String.valueOf(chatId), localeMessageService.getMessage(replyMessage, args));
    }

    public String getReplyText(String replyText){
        return localeMessageService.getMessage(replyText);
    }
}

