package com.clhost.weatherbot.bot;

public class MessageConstants {
    //todo: поправить погодные константы на вывод города
    private static final String COMMANDS =
            "/start - список команд\n" +
            "/subscribe - оформить подписку на уведомления об изменении погоды\n" +
            "/unsubscribe - отписаться от уведомлений\n" +
            "/show - показать текущие подписки";
    static final String START_1 =
            "Привет, %s! Я - бот, созданный в рамках тестового задания " +
            "на стажировку в компанию Yota. Я помогу вам отслеживать изменения погоды " +
            "в любом городе, который вы укажете. Список доступных команд для вас: \n" + COMMANDS;
    static final String START_2 =
            "Привет, %s. Итак, давайте по порядку, я - бот, который поможет вам " +
            "отслеживать изменения о погоде. Как со мной взаимодействовать: \n" + COMMANDS;
    static final String START_3 =
            "Хотите я буду уведомлять вас об изменении погоды заранее? Тогда, %s, " +
            "я создан для вас! Обращаться со мной совсем просто, вот: " + COMMANDS;
    static final String AFTER_START =
            "Ну нет, погодите, я не знаю, чего вы хотите. Скажите мне об этом явно: \n" + COMMANDS;
    static final String INVALID_SUBSCRIBE_INFO =
            "\"%s\" не является числом, попробуйте ввести снова:";
    static final String INVALID_CITY =
            "Для города \"%s\" я ничего не нашел. Может быть, его не существует или в его названии присутствует " +
                    "ошибка? Попробуйте снова:";
    static final String INVALID_HOUR =
            "Количество часов должно быть больше 0 и меньше 24. Попробуйте снова:";
    static final String INVALID_UNSUBSCRIBE =
            "Для города \"%s\" у вас не было подписок.";
    static final String SUCCESSFUL_UNSUB =
            "Вы успешно отписались от уведомлений по изменению погоды в городе \"%s\".";
    static final String SUBSCRIBE =
            "Напишите город и за какое время (в часах) следует вас уведомить:";
    static final String UNSUBSCRIBE =
            "Напишите город, от которого мне следует вас отписать:";
    static final String SUCCESSFUL_SUB =
            "Отлично! За %d %s, я сообщу, когда погода в городе \"%s\" изменится.";
    static final String EMPTY_SUBS =
            "Вы еще не подписаны ни на одно обновление погоды.";
    static final String SHOW_SUBS =
            "Ваши подписки: \n%s";


    public static final String THUNDERSTORM =
            "Ого, шторм надвигается. Приготовьтесь, так как через %d %s начнет штормить :(";
    public static final String DRIZZLE =
            "Через %d %s ожидается изморось.";
    public static final String RAIN =
            "Не выходите на улицу, если вы не любитель дождя! Через %d %s он ожидается.";
    public static final String SNOW =
            "Снежок.. Белый, пушистый, хрустящий.. Через %d %s в вашем городе!";
    public static final String ATMOSPHERE =
            "Атмосферно у вас сегодня будет, совсем скоро, подождите %d %s.";
    public static final String CLEAR =
            "Спустя %d %s вас ожидает ясность на небе!";
    public static final String CLOUDS =
            "Перистые или кучевые? А может слоистые? Взгляните в окно через %d %s.";
}
