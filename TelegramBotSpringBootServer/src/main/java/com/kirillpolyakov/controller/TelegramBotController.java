package com.kirillpolyakov.controller;

import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.TelegramRequest;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.kirillpolyakov.config.Compliment;
import com.kirillpolyakov.model.*;
import com.kirillpolyakov.service.*;
import com.kirillpolyakov.util.Constants;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.StringJoiner;

@BotController
public class TelegramBotController implements TelegramMvcController {
    @Value("${bot.token}")
    private String token;
    private Keyboard replyKeyboardMarkupRegister;

    private Keyboard replyKeyboardMarkupRegisterNextAllPhotos;

    private UserService userService;

    private ComplimentService complimentService;

    private UserComplimentService userComplimentService;

    private HistoryService historyService;

    private MyMessageService myMessageService;

    @Autowired
    public void setMyMessageService(MyMessageService myMessageService) {
        this.myMessageService = myMessageService;
    }

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Autowired
    public void setUserComplimentService(UserComplimentService userComplimentService) {
        this.userComplimentService = userComplimentService;
    }

    @Autowired
    public void setComplimentService(ComplimentService complimentService) {
        this.complimentService = complimentService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        complimentService.addNotInDB();
        this.replyKeyboardMarkupRegister = new ReplyKeyboardMarkup(
                "/register")
                .oneTimeKeyboard(true)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);
        this.replyKeyboardMarkupRegisterNextAllPhotos = new ReplyKeyboardMarkup(
                "/next", "/all", "/photos")
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .selective(true);
    }

    @Override
    public String getToken() {
        return this.token;
    }

    private SendMessage sendMessageWithButtons(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.replyMarkup(this.createKeyBoard(userService.getByChatId(chatId)));
        return sendMessage.parseMode(ParseMode.HTML);
    }

    private SendPhoto sendImageWithButtons(long chatId, String fileName) {
        SendPhoto sendPhoto = new SendPhoto(chatId, new File(fileName));
        sendPhoto.replyMarkup(this.createKeyBoard(userService.getByChatId(chatId)));
        return sendPhoto.parseMode(ParseMode.HTML);
    }

    /**
     * Callback for /start message
     */
    @BotRequest(value = "/start", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest start(User user, Chat chat, TelegramRequest telegramRequest) {
        UserTelegram userTelegram = this.userService.getByChatId(chat.id());
        String response;
        if (userTelegram == null) {
            this.userService.add(new UserTelegram(chat.id(), user.username()));
            userTelegram = this.userService.getByChatId(chat.id());
            this.historyService.add(new History("/start"), userTelegram.getId());
            response = MessageFormat.format("Hello! {0} \uD83D\uDD25 Welcome to <b>my</b> bot!\nPlease" +
                    "push /register to sign up", user.username());
        } else if (userTelegram.getStep() == null) {
            response = MessageFormat.format("Hello! {0} \uD83D\uDD25 Welcome to <b>my</b> bot!\nPlease" +
                    "push /register to sign up", user.username());
        } else if (userTelegram.getStep().equals(Step.PREREG)) {
            response = "Last time you haven't finished registration" +
                    "\nPlease enter your login";
        } else if (userTelegram.getStep().equals(Step.LOGIN)) {
            response = "Last time you haven't finished registration" +
                    "\nYour login: " + userTelegram.getLogin() + "\nPlease enter your name";
        } else if (userTelegram.getStep().equals(Step.NAME)) {
            response = "Last time you haven't finished registration" +
                    "\nYour login: " + userTelegram.getLogin() + "\nYour name: " + userTelegram.getName()
                    + "\nPlease enter your age";
        } else if (userTelegram.getStep().equals(Step.AGE) || userTelegram.getStep().equals(Step.PHOTO)) {
            response = MessageFormat.format("Hello! {0} \uD83D\uDD25 Welcome to <b>my</b> bot!",
                    userTelegram.getTelegramUserName());
        } else {
            response = "Unknown message";
        }
        this.myMessageService.add(new MyMessage(telegramRequest.getText(), telegramRequest.getMessage().messageId()),
                userTelegram.getId());
        this.addMyMessageResponse(telegramRequest, userTelegram, response);
        return sendMessageWithButtons(chat.id(), response);
    }

    /**
     * Callback for /register message
     */
    @BotRequest(value = "/register", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest register(User user, Chat chat, TelegramRequest telegramRequest) {
        UserTelegram userTelegram = this.userService.getByChatId(chat.id());
        this.historyService.add(new History("/register"), userTelegram.getId());
        String response;
        if (userTelegram.getStep() == null) {
            UserTelegram newUserTelegram = new UserTelegram(userTelegram.getChatId(), userTelegram.getTelegramUserName(),
                    Step.PREREG);
            this.userService.update(newUserTelegram);
            response = "Please, enter your login";
        } else if (userTelegram.getStep().equals(Step.PREREG)) {
            response = "Please, enter your login";
        } else if (userTelegram.getStep().equals(Step.LOGIN)) {
            response = "Please, enter your name";
        } else if (userTelegram.getStep().equals(Step.NAME)) {
            response = "Please, enter your age";
        } else {
            response = "You are already registered";
        }
        this.myMessageService.add(new MyMessage(telegramRequest.getText(), telegramRequest.getMessage().messageId()),
                userTelegram.getId());
        this.addMyMessageResponse(telegramRequest, userTelegram, response);
        return sendMessageWithButtons(chat.id(), response);
    }

    /**
     * Callback for /photos message
     */
    @BotRequest(value = "/photos", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest listPhotos(User user, Chat chat, TelegramRequest telegramRequest) {
        UserTelegram userTelegram = this.userService.getByChatId(chat.id());
        String response;
        this.historyService.add(new History("/photos"), userTelegram.getId());
        if (userTelegram.getStep() == null) {
            response = "Please, push /register";
        } else if (userTelegram.getStep().equals(Step.PREREG)) {
            response = "Please, enter your login";
        } else if (userTelegram.getStep().equals(Step.LOGIN)) {
            response = "Please, enter your name";
        } else if (userTelegram.getStep().equals(Step.NAME)) {
            response = "Please, enter your age";
        } else {
            UserTelegram newUserTelegram = new UserTelegram(userTelegram.getChatId(), userTelegram.getTelegramUserName(),
                    userTelegram.getLogin(), userTelegram.getName(), userTelegram.getAge(), Step.PHOTO);
            this.userService.update(newUserTelegram);
            StringJoiner stringJoiner = new StringJoiner(", ");
            Path path = Path.of(Constants.PATH);
            try {
                Files.walkFileTree(path, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        stringJoiner.add(file.getFileName().toString());
                        return super.visitFile(file, attrs);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            response = "Please, to chose file please write filename from: "
                    + stringJoiner;
        }
        this.myMessageService.add(new MyMessage(telegramRequest.getText(), telegramRequest.getMessage().messageId()),
                userTelegram.getId());
        this.addMyMessageResponse(telegramRequest, userTelegram, response);
        return sendMessageWithButtons(chat.id(), response);
    }

    /**
     * Callback for /next message
     */
    @BotRequest(value = "/next", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest next(Chat chat, TelegramRequest telegramRequest) {
        UserTelegram userTelegram = this.userService.getByChatId(chat.id());
        String response;
        this.historyService.add(new History("/next"), userTelegram.getId());
        if (userTelegram.getStep() == null) {
            response = "Please, push /register";
        } else if (userTelegram.getStep().equals(Step.PREREG)) {
            response = "Please, enter your login";
        } else if (userTelegram.getStep().equals(Step.LOGIN)) {
            response = "Please, enter your name";
        } else if (userTelegram.getStep().equals(Step.NAME)) {
            response = "Please, enter your age";
        } else if (this.userComplimentService.getByUserId(userTelegram.getChatId()).size()
                == this.complimentService.getAll().size()) {
            this.userComplimentService.deleteAllByUserTelegramId(userTelegram.getId());
            response = this.getRandomCompliment(userTelegram);
        } else {
            response = this.getRandomCompliment(userTelegram);
        }
        this.myMessageService.add(new MyMessage(telegramRequest.getText(), telegramRequest.getMessage().messageId()),
                userTelegram.getId());
        this.addMyMessageResponse(telegramRequest, userTelegram, response);
        return this.sendMessageWithButtons(chat.id(), response);
    }

    /**
     * Callback for /all message
     */
    @BotRequest(value = "/all", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest allCompliments(Chat chat, TelegramRequest telegramRequest) {
        UserTelegram userTelegram = this.userService.getByChatId(chat.id());
        String response;
        this.historyService.add(new History("/all"), userTelegram.getId());
        if (userTelegram.getStep() == null) {
            response = "Please, push /register";
        } else if (userTelegram.getStep().equals(Step.PREREG)) {
            response = "Please, enter your login";
        } else if (userTelegram.getStep().equals(Step.LOGIN)) {
            response = "Please, enter your name";
        } else if (userTelegram.getStep().equals(Step.NAME)) {
            response = "Please, enter your age";
        } else {
            StringJoiner stringJoiner = new StringJoiner(", ");
            this.complimentService.getAll().forEach(x -> stringJoiner.add(x.getText()));
            response = stringJoiner.toString();
        }
        this.myMessageService.add(new MyMessage(telegramRequest.getText(),
                        telegramRequest.getMessage().messageId()),
                userTelegram.getId());
        this.addMyMessageResponse(telegramRequest, userTelegram, response);
        return sendMessageWithButtons(chat.id(), response);
    }

    /**
     * Callback for other messages
     */
    @BotRequest(value = "{message:[\\S ]+}", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest all(@BotPathVariable("message") String text, Chat chat, TelegramRequest telegramRequest) {
        UserTelegram userTelegram = this.userService.getByChatId(chat.id());
        String response;
        this.historyService.add(new History(text), userTelegram.getId());
        this.myMessageService.add(new MyMessage(telegramRequest.getText(), telegramRequest.getMessage().messageId()),
                userTelegram.getId());
        if (userTelegram.getStep() == null) {
            response = "Please, push /register";
        } else if (userTelegram.getStep().equals(Step.PREREG)) {
            UserTelegram newUserTelegram = new UserTelegram(userTelegram.getChatId(), userTelegram.getTelegramUserName(),
                    text, Step.LOGIN);
            this.userService.update(newUserTelegram);
            response = "Please, enter your name";
        } else if (userTelegram.getStep().equals(Step.LOGIN)) {
            UserTelegram newUserTelegram = new UserTelegram(userTelegram.getChatId(), userTelegram.getTelegramUserName(),
                    userTelegram.getLogin(), text, Step.NAME);
            this.userService.update(newUserTelegram);
            response = "Please, enter your age";
        } else if (userTelegram.getStep().equals(Step.NAME)) {
            UserTelegram newUserTelegram;
            try {
                newUserTelegram = new UserTelegram(userTelegram.getChatId(), userTelegram.getTelegramUserName(),
                        userTelegram.getLogin(), userTelegram.getName(), Integer.parseInt(text), Step.AGE);
            } catch (NumberFormatException e) {
                response = "Age should consist only of digits\n" +
                        "Please, enter your age";
                this.addMyMessageResponse(telegramRequest, userTelegram, response);
                return sendMessageWithButtons(chat.id(), response);
            }
            this.userService.update(newUserTelegram);
            response = "Registration is successfully finished";
        } else if (userTelegram.getStep().equals(Step.PHOTO)) {
            File file = new File(Constants.PATH + "\\" + text);
            if (file.exists()) {
                addMyMessageResponse(telegramRequest, userTelegram, text);
                return sendImageWithButtons(chat.id(), Constants.PATH + "\\" + text);
            }
            response = "File with such filename doesn't exist";
        } else {
            response = "Unknown message";
        }
        this.addMyMessageResponse(telegramRequest, userTelegram, response);
        return sendMessageWithButtons(chat.id(), response);
    }

    public String getRandomCompliment(UserTelegram userTelegram) {
        Random random = new Random();
        int id = random.nextInt(this.complimentService.getAll().size()) + 1;
        Compliment compliment = this.complimentService.get(id);
        UserCompliment userCompliment = new UserCompliment(compliment.getText(), LocalDateTime.now());
        try {
            this.userComplimentService.add(userCompliment, userTelegram.getChatId());
        } catch (IllegalArgumentException e) {
            return this.getRandomCompliment(userTelegram);
        }
        return compliment.getText();
    }

    private Keyboard createKeyBoard(UserTelegram userTelegram) {
        if (userTelegram == null) {
            return this.replyKeyboardMarkupRegister;
        } else if (userTelegram.getStep() == null) {
            return this.replyKeyboardMarkupRegister;
        }
        return this.replyKeyboardMarkupRegisterNextAllPhotos;
    }

    private void addMyMessageResponse(TelegramRequest telegramRequest, UserTelegram userTelegram, String response) {
        telegramRequest.setCallback(new Callback() {
            @Override
            public void onResponse(BaseRequest baseRequest, BaseResponse baseResponse) {
                int messageId = ((SendResponse) baseResponse).message().messageId();
                myMessageService.add(new MyMessage(response, messageId), userTelegram.getId());
            }

            @Override
            public void onFailure(BaseRequest baseRequest, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
