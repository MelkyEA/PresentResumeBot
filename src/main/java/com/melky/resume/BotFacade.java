package com.melky.resume;

import com.melky.resume.Service.ReplyMessageService;
import com.melky.resume.answer.Callback;
import com.melky.resume.answer.ProjectsAnswer;
import com.melky.resume.answer.ResumeAnswer;
import com.melky.resume.menu.MainMenu;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.io.FileNotFoundException;

@Component
public class BotFacade {

    private ReplyMessageService replyMessageService;
    private MainMenu mainMenu;
    private ResumeAnswer resumeAnswer;
    private ProjectsAnswer projectsAnswer;
    private Callback callback;
    private Bot bot;

    public BotFacade(ReplyMessageService replyMessageService, MainMenu mainMenu, ResumeAnswer resumeAnswer,
                     ProjectsAnswer projectsAnswer, Callback callback, @Lazy Bot bot) {
        this.replyMessageService = replyMessageService;
        this.mainMenu = mainMenu;
        this.resumeAnswer = resumeAnswer;
        this.projectsAnswer = projectsAnswer;
        this.callback = callback;
        this.bot = bot;
    }

    public BotApiMethod<?> handleUpdate(Update update){

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            if(callbackQuery.getData().equals("uploadResume"))
                bot.sendResume(callbackQuery.getMessage().getChatId());
        }



        SendMessage replyMessage = null;
        Message message = update.getMessage();
        if(message != null && message.hasText()){
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message){
        String inputMsg = message.getText();
       long chatId = message.getChatId();
        SendMessage replyMessage;

        switch (inputMsg.toLowerCase()){
            case "/start":
                replyMessage = mainMenu.getMainMenuMessage(chatId);
                break;
            case "мой опыт":
                replyMessage = resumeAnswer.getAnswerResume(chatId);
                break;
            case "мои проекты":
                replyMessage = projectsAnswer.getAnswerProjects(chatId);
                break;
            case "связь со мной":
                replyMessage = callback.getAnswerProjects(chatId);
                break;
            default:
                replyMessage = new SendMessage(String.valueOf(chatId), "Выберите раздел ниже.");
                break;
        }
        replyMessage.setParseMode("HTML");
        return replyMessage;
    }





}
