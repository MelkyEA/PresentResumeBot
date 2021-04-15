package com.melky.resume;

import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;

public class Bot extends TelegramWebhookBot {

    private String botName;
    private String botToken = System.getenv("TOKEN");;
    private String webhookPath;

    private BotFacade botFacade;

    public Bot(BotFacade botFacade) {
        this.botFacade = botFacade;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        BotApiMethod<?> replyMessage = botFacade.handleUpdate(update);

        return replyMessage;
    }

    @Override
    public String getBotPath() {
        return webhookPath;
    }
    public void setBotName(String botName) {
        this.botName = botName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void setWebhookPath(String webhookPath) {
        this.webhookPath = webhookPath;
    }

    public void sendResume(long chatId){
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(String.valueOf(chatId));

        try {
            File resume = null;
            resume = ResourceUtils.getFile("classpath:static/doc/resume.doc");

            sendDocument.setDocument(new InputFile(resume));
            execute(sendDocument);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }
}
