package com.melky.bot.telegram;

import com.melky.bot.telegram.Service.ReplyMessageService;
import com.melky.bot.telegram.answer.Callback;
import com.melky.bot.telegram.answer.ProjectsAnswer;
import com.melky.bot.telegram.answer.ResumeAnswer;
import com.melky.bot.telegram.menu.MainMenu;
import com.melky.bot.vk.handlers.CallbackAPIHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotFacade {

    private ReplyMessageService replyMessageService;
    private MainMenu mainMenu;
    private ResumeAnswer resumeAnswer;
    private ProjectsAnswer projectsAnswer;
    private Callback callback;
    private Bot bot;
    private CallbackAPIHandler callbackAPIHandler;

    public BotFacade(ReplyMessageService replyMessageService, MainMenu mainMenu, ResumeAnswer resumeAnswer,
                     ProjectsAnswer projectsAnswer, Callback callback, @Lazy Bot bot,
                     CallbackAPIHandler callbackAPIHandler) {
        this.replyMessageService = replyMessageService;
        this.mainMenu = mainMenu;
        this.resumeAnswer = resumeAnswer;
        this.projectsAnswer = projectsAnswer;
        this.callback = callback;
        this.bot = bot;
        this.callbackAPIHandler = callbackAPIHandler;
    }

    public BotApiMethod<?> handleUpdate(Update update){

//        if (update.hasCallbackQuery()) {
//            CallbackQuery callbackQuery = update.getCallbackQuery();
//            if(callbackQuery.getData().equals("uploadResume"))
//                bot.sendResume(callbackQuery.getMessage().getChatId());
//        }



        SendMessage replyMessage = null;
        Message message = update.getMessage();
        if(message != null && message.hasText()){
            replyMessage = handleInputMessage(message);
        }
        //callbackAPIHandler.messageNew("UsedTelegram",11826072);
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message){
        String inputMsg = message.getText();
       long chatId = message.getChatId();
        SendMessage replyMessage;

        switch (inputMsg.toLowerCase()){
            case "/start":
                System.out.println("???????????????????????? ?? ?????????? @"
                        + message.getFrom().getUserName() + "?????????? ?????????????? ?? ??????????");
                replyMessage = mainMenu.getMainMenuMessage(chatId);
                break;
            case "?????? ????????":
                System.out.println("???????????????????????? ?? ?????????? @"
                        + message.getFrom().getUserName() + "?????????? ???? ???????????? ?? ????????????");
                replyMessage = resumeAnswer.getAnswerResume(chatId);
                break;
            case "?????? ??????????????":
                System.out.println("???????????????????????? ?? ?????????? @"
                        + message.getFrom().getUserName() + "?????????? ???? ???????????? ?? ??????????????????");
                replyMessage = projectsAnswer.getAnswerProjects(chatId);
                break;
            case "?????????? ???? ????????":
                System.out.println("???????????????????????? ?? ?????????? @"
                        + message.getFrom().getUserName() + "?????????? ???? ???????????? ???? ????????????");
                replyMessage = callback.getAnswerProjects(chatId);
                break;
            default:
                replyMessage = new SendMessage(String.valueOf(chatId), "???????????????? ???????????? ????????.");
                break;
        }
        replyMessage.setParseMode("HTML");
        return replyMessage;
    }





}
