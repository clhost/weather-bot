package com.clhost.weatherbot.bot;

import com.clhost.weatherbot.entity.ForecastData;
import com.clhost.weatherbot.entity.Subscription;
import com.clhost.weatherbot.logger.Logging;
import com.clhost.weatherbot.services.ForecastService;
import com.clhost.weatherbot.services.MessageSender;
import com.clhost.weatherbot.services.NotificationService;
import com.clhost.weatherbot.services.SubscribeService;
import com.clhost.weatherbot.utils.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component
public class WeatherBot extends TelegramLongPollingBot {
    private static final int DISCARD_MESSAGE_DURATION = 5; // в секундах
    private static final String START = "/start";
    private static final String SHOW = "/show";
    private static final String SUBSCRIBE = "/subscribe";
    private static final String UNSUBSCRIBE = "/unsubscribe";
    private static final String[] START_ARR = {
            MessageConstants.START_1,
            MessageConstants.START_2,
            MessageConstants.START_3
    };
    private static final Map<Long, InputState> states = new HashMap<>();

    @Value("${telegram.app.name}")
    private String name;

    @Value("${telegram.app.token}")
    private String token;

    @Logging
    private Logger logger;

    private SubscribeService subscribeService;
    private ForecastService forecastService;
    private MessageSender messageSender;
    private NotificationService notificationService;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    public void setSubscribeService(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @Autowired
    public void setForecastService(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void startNotifier() {
        executorService.submit(() -> notificationService.loop());
        logger.info("Notification service has been started.");
    }

    @PreDestroy
    public void stopNotifier() {
        notificationService.interrupt();
        logger.info("Notification service has been interrupted.");
    }

    public WeatherBot(DefaultBotOptions options) {
        super(options);
    }

    public WeatherBot() {}

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("Incoming message from user [id:" + update.getMessage().getFrom().getId() +
                ", name:" + update.getMessage().getFrom().getFirstName() + "]: " + update.getMessage());
        if (update.hasMessage() && update.getMessage().getText() != null) {
            int msgDate = update.getMessage().getDate();
            int currentDate = (int) (DateTime.now().getMillis() / 1000);
            boolean isDiscard = (currentDate - msgDate) > DISCARD_MESSAGE_DURATION * 60;

            if (!isDiscard) {
                registerUser(update.getMessage().getChatId(), update.getMessage().getFrom().getId());
                action(update);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    private void sendMessage(Long chatId, String message) {
        try {
            execute(new SendMessage(chatId, message));
            logger.info("Sent message to chat [id:" + chatId + "]: " + message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void action(@NotNull final Update update) {
        Long userId = Long.valueOf(update.getMessage().getFrom().getId());
        Long chatId = update.getMessage().getChatId();
        String command = update.getMessage().getText().trim();

        switch (command) {
            case START:
                sendMessage(chatId, String.format(
                        START_ARR[new Random().nextInt(START_ARR.length - 1)],
                        update.getMessage().getFrom().getFirstName()));
                states.put(userId, InputState.START);
                break;
            case SHOW:
                showSubscriptions(userId, chatId);
                states.put(userId, InputState.START);
                break;
            case SUBSCRIBE:
                sendMessage(chatId, MessageConstants.SUBSCRIBE);
                states.put(userId, InputState.SUBSCRIBE);
                break;
            case UNSUBSCRIBE:
                sendMessage(chatId, MessageConstants.UNSUBSCRIBE);
                states.put(userId, InputState.UNSUBSCRIBE);
                break;
            default:
                InputState state = states.get(userId);
                switch (state) {
                    case START:
                        sendMessage(chatId, MessageConstants.AFTER_START);
                        break;
                    case SUBSCRIBE:
                        subscribe(chatId, userId, command.split(" "));
                        break;
                    case UNSUBSCRIBE:
                        unsubscribe(chatId, userId, command);
                        break;
                }
        }
    }

    private void showSubscriptions(long userId, long chatId) {
        List<Subscription> subscriptions = subscribeService.showSubscriptions(userId);
        if (subscriptions.isEmpty()) {
            sendMessage(chatId,  MessageConstants.EMPTY_SUBS);
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (Subscription sub : subscriptions) {
            builder.append("Город: ").append(sub.getCity())
                    .append(". Время: ").append(sub.getHours()).append(" ")
                    .append(StringUtils.getHourStringByHour(sub.getHours())).append(".\n");
        }
        sendMessage(chatId, String.format(
                MessageConstants.SHOW_SUBS,
                builder.toString()
        ));
    }

    private void subscribe(long chatId, long userId, String[] tokens) {
        String hours = tokens[tokens.length - 1];
        if (StringUtils.isNumeric(hours)) {
            int hour = Integer.parseInt(hours);

            if (hour > 24 || hour < 1) {
                sendMessage(chatId, MessageConstants.INVALID_HOUR);
                return;
            }

            String city = tokens[0];
            if (tokens.length > 2) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < tokens.length - 1; i++) {
                    if (i == tokens.length - 2) {
                        builder.append(tokens[i]);
                    } else {
                        builder.append(tokens[i]).append(" ");
                    }
                }
                city = builder.toString();
            }

            List<ForecastData> dataList = forecastService.getForecastByCity(city);
            if (dataList.isEmpty()) {
                sendMessage(chatId, String.format(
                        MessageConstants.INVALID_CITY,
                        city
                ));
                return;
            }

            sendMessage(chatId, String.format(
                    MessageConstants.SUCCESSFUL_SUB, hour,
                    StringUtils.getHourStringByHour(hour),
                    city
            ));

            // подписался
            Subscription sub = new Subscription(userId, Integer.parseInt(hours), city);
            subscribeService.subscribe(sub);
            states.put(userId, InputState.START);
        } else {
            sendMessage(chatId, String.format(
                    MessageConstants.INVALID_SUBSCRIBE_INFO,
                    hours
            ));
        }
    }

    private void unsubscribe(long chatId, long userId, String city) {
        boolean isUnsubscribe = subscribeService.unsubscribe(userId, city);
        if (!isUnsubscribe) {
            sendMessage(chatId, String.format(
                    MessageConstants.INVALID_UNSUBSCRIBE,
                    city
            ));
            return;
        }
        sendMessage(chatId, String.format(
                MessageConstants.SUCCESSFUL_UNSUB,
                city
        ));
        states.put(userId, InputState.START);
        logger.info("User [id:" + userId + "] was remove subscription for city: " + city);
    }

    // зарегистрировать юзера
    private void registerUser(long chatId, long userId) {
        messageSender.register(userId, chatId);
    }

    enum InputState {
        START,
        SUBSCRIBE,
        UNSUBSCRIBE,
    }
}
