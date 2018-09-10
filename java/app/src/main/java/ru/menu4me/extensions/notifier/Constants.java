package ru.menu4me.extensions.notifier;

public class Constants {
    public static final String TAG = "Notifier";

    public static final String RESOURCE_DRAWABLE = "drawable";
    public static final String RESOURCE_ICON_NOTIFICATION = "icon";

    //public static final int PERIOD_HALF_MINUTE = 30000;
    public static final int PERIOD_MINUTE = 60000;

    public static final String HASH_SHA256 = "SHA-256";

    public static final String EVENT_FROM_NOTIFICATION = "fromNotificationEvent";
    public static final String EVENT_FOREGROUND_NOTIFICATION = "foregroundNotificationEvent";

    /**
     * Файл со всеми данными приложения
     */
    public static final String DATA_FILENAME = "data.json";

    public static final String EXTRA_REQUEST = "extraRequest";

    // TODO: paste link to api here
    public static final String SERVER_API_URL = "link_to_api";

    public static final String LOCAL_SERVER_API_URL = "link_to_api";

    /**
     * Серверный сервис уведомлений
     */
    public static final String SERVER_SERVICE_NOTIFICATION_CONTROLLER = "NotificationController";

    /**
     * Серверный сервис авторизации
     */
    public static final String SERVER_SERVICE_CLIENT_AUTH = "ClientAuthService";

    /**
     * Серверный метод для получения массива уведомлений
     */
    public static final String SERVER_METHOD_GET_CUSTOM_NOTIFICATION = "GetCustomNotifications";

    /**
     * Серверный метод для отметки полученных уведомлений
     */
    public static final String SERVER_METHOD_SET_CUSTOM_RECEIVED = "SetCustomReceived";

    /**
     * Серверный метод для авторизации на сервере
     */
    public static final String SERVER_METHOD_CREATE_TOKEN_BY_PASSWORD = "CreateTokenByPassword";
    /**
     * Серверный метод для обновления токена
     */
    public static final String SERVER_METHOD_REFRESH_TOKEN = "RefreshToken";

    public static final String ACTION_UPDATE = "ru.menu4me.extensions.notifier.action.UPDATE";

    public static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
}
