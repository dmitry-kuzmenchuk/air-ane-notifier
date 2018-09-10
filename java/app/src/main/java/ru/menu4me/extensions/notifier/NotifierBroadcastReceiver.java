package ru.menu4me.extensions.notifier;

import android.app.*;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotifierBroadcastReceiver extends BroadcastReceiver
{
    private static final String tag = "Notifier Broadcast Receiver";

    private OkHttpClient httpClient = null;
    private static final MediaType MEDIATYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private AppData appData = null;
    private Gson gson;

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Log.d(Constants.TAG, tag + "onReceive() " + intent.getAction());
        Logger.log(tag, "Получено сообщение: " + intent.getAction());
        this.context = context;
        appData = AppData.readFromStorage(context, tag);
        gson = new Gson();

        switch (intent.getAction())
        {
            case Constants.ACTION_UPDATE:
            {
                if (appData != null) update();
                break;
            }
            case Constants.ACTION_BOOT_COMPLETED:
            {
                //Logger.exc(tag, "YAHOOO");
                RegisterUpdates.register(context);
                if (appData != null) update();
                break;
            }
        }
    }

    /**
     * Метод делает запросы к серверу для получения уведомлений приложения.
     *
     * Нет токена ? получаем токен.
     * Токен просрочен ? получаем обновленный токен.
     * После, запрашиваем список уведомлений.
     */
    private void update()
    {
        if (appData.token == "")
        {
            authorize();
        }
        else if (appData.isOutdated)
        {
            refreshToken();
        }
        else
        {
            fetchUpdates();
        }
    }

    /**
     * Получает токен и авторизируется на сервере
     */
    private void authorize()
    {
        Logger.log(tag, "Авторизация. . .");
        RequestObject ro = new RequestObject(Constants.SERVER_SERVICE_CLIENT_AUTH, Constants.SERVER_METHOD_CREATE_TOKEN_BY_PASSWORD);
        ro.buildAuthRequest(appData);

        try
        {
            sendPost(Constants.SERVER_API_URL, gson.toJson(ro, RequestObject.class));
        }
        catch (Exception e)
        {
            Logger.exc(tag, "Ошибка авторизации");
            Logger.exc(tag, e.toString());
        }
    }

    /**
     * Обновляет токен на сервере и в приложении
     */
    private void refreshToken()
    {
        Logger.log(tag, "Обновление токена. . .");
        RequestObject ro = new RequestObject(Constants.SERVER_SERVICE_CLIENT_AUTH, Constants.SERVER_METHOD_REFRESH_TOKEN);
        ro.buildRefreshTokenRequest(appData);

        try
        {
            sendPost(Constants.SERVER_API_URL, gson.toJson(ro, RequestObject.class));
        }
        catch (Exception e)
        {
            Logger.exc(tag, "Ошибка обновления токена");
            Logger.exc(tag, e.toString());
        }
    }

    /**
     * Получает с сервера список уведомлений, требующих отображения
     */
    private void fetchUpdates()
    {
        Logger.log(tag, "Получение обновлений. . .");
        RequestObject ro = new RequestObject(Constants.SERVER_SERVICE_NOTIFICATION_CONTROLLER, Constants.SERVER_METHOD_GET_CUSTOM_NOTIFICATION);
        ro.buildGetNotificationsRequest(appData);

        try
        {
            sendPost(Constants.SERVER_API_URL, gson.toJson(ro, RequestObject.class));
        }
        catch (Exception e)
        {
            Logger.exc(tag, "Ошибка получения обновлений");
            Logger.exc(tag, e.toString());
        }
    }

    /**
     * Отправляет запрос на сервер и обрабатывает его ответ в onResponse()
     * @param url ссылка к api сервера
     * @param json отправляемые данные
     */
    public void sendPost(String url, String json) throws Exception
    {
        Logger.log(tag, "Отправка запроса. . .");
        Logger.log(tag, "Тело запроса: " + json);

        if (httpClient == null) httpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();

        RequestBody body = RequestBody.create(MEDIATYPE_JSON, json);
        Request request = new Request.Builder().url(url).post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("Connection", "close")
                .build();

        httpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                Logger.exc(tag, "Ошибка передачи/получения запроса");
                Logger.exc(tag, e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                try
                {
                    if (!response.isSuccessful())
                    {
                        throw new IOException("Unexpected code " + response);
                    }

                    String responseResult = response.body().string();
                    ServerResult serverResponse = gson.fromJson(responseResult, ServerResult.class);

                    switch (serverResponse.resultCode)
                    {
                        // Список уведомлений получен
                        case "nfc_800":
                        {
                            Logger.log(tag, "Cписок уведомлений для отображения успешно получен");
                            Logger.log(tag, "Тело ответа: " + responseResult);

                            if (serverResponse.data.items.size() < 1)
                            {
                                Logger.log(tag, "Уведомлений для отображения - нет");
                                if (Extension.context != null && Extension.isInForeground)
                                {
                                    Logger.log(tag, "Приложение активно в данный момент - в него отправляется событие");
                                    Extension.context.dispatchStatusEventAsync(Constants.EVENT_FOREGROUND_NOTIFICATION, responseResult);
                                }
                                return;
                            }
                            else
                            {
                                Logger.log(tag, "Доступны уведомления для отображения");
                                if (Extension.context != null && Extension.isInForeground)
                                {
                                    Logger.log(tag, "Приложение активно в данный момент - в него отправляется событие");
                                    Extension.context.dispatchStatusEventAsync(Constants.EVENT_FOREGROUND_NOTIFICATION, responseResult);

                                    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                                    int volume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
                                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    final MediaPlayer mediaPlayer = new MediaPlayer();

                                    try
                                    {
                                        mediaPlayer.setDataSource(context, soundUri);
                                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                                        {
                                            @Override
                                            public void onCompletion(MediaPlayer mp)
                                            {
                                                mp.release();
                                            }
                                        });

                                        mediaPlayer.prepare();
                                        mediaPlayer.setVolume(volume, volume);
                                        mediaPlayer.start();
                                    }
                                    catch (Exception e)
                                    {
                                        Logger.exc(tag, e.toString());
                                        mediaPlayer.release();
                                    }

                                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                                    vibrator.vibrate(2000);

                                    int index = 0;
                                    int[] items = new int[serverResponse.data.items.size()];
                                    for (NotificationData nd : serverResponse.data.items)
                                    {
                                        if (index < 5)
                                        {
                                            showNotification(context, nd.title, nd.alert, "Новый заказ", nd.id, responseResult);
                                        }
                                        items[index] = nd.id;
                                        index++;
                                    }

                                    Logger.log(tag, "Подтверждение полученных уведомлений успешно. . .");
                                    RequestObject ro = new RequestObject(Constants.SERVER_SERVICE_NOTIFICATION_CONTROLLER, Constants.SERVER_METHOD_SET_CUSTOM_RECEIVED);
                                    ro.buildSetNotificationReceivedRequest(appData, items);

                                    try
                                    {
                                        sendPost(Constants.SERVER_API_URL, gson.toJson(ro, RequestObject.class));
                                    }
                                    catch (Exception e)
                                    {
                                        Logger.exc(tag, "Ошибка подтверждения полученных уведомлений");
                                        Logger.exc(tag, e.toString());
                                    }
                                }
                                else
                                {
                                    Logger.log(tag, "Приложение не активно в данный момент - обработка событий не требуется");

                                    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                                    int volume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
                                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    final MediaPlayer mediaPlayer = new MediaPlayer();

                                    try
                                    {
                                        mediaPlayer.setDataSource(context, soundUri);
                                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                                        {
                                            @Override
                                            public void onCompletion(MediaPlayer mp)
                                            {
                                                mp.release();
                                            }
                                        });

                                        mediaPlayer.prepare();
                                        mediaPlayer.setVolume(volume, volume);
                                        mediaPlayer.start();
                                    }
                                    catch (Exception e)
                                    {
                                        Logger.exc(tag, e.toString());
                                        mediaPlayer.release();
                                    }

                                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                                    vibrator.vibrate(2000);

                                    int index = 0;
                                    int[] items = new int[serverResponse.data.items.size()];
                                    for (NotificationData nd : serverResponse.data.items)
                                    {
                                        if (index < 5)
                                        {
                                            showNotification(context, nd.title, nd.alert, "Новый заказ", nd.id, responseResult);
                                        }
                                        items[index] = nd.id;
                                        index++;
                                    }

                                    Logger.log(tag, "Подтверждение полученных уведомлений успешно. . .");
                                    RequestObject ro = new RequestObject(Constants.SERVER_SERVICE_NOTIFICATION_CONTROLLER, Constants.SERVER_METHOD_SET_CUSTOM_RECEIVED);
                                    ro.buildSetNotificationReceivedRequest(appData, items);
                                    try
                                    {
                                        sendPost(Constants.SERVER_API_URL, gson.toJson(ro, RequestObject.class));
                                    }
                                    catch (Exception e)
                                    {
                                        Logger.exc(tag, "Ошибка подтверждения полученных уведомлений");
                                        Logger.exc(tag, e.toString());
                                    }
                                }
                            }
                            break;
                        }

                        case "nfc_900":
                        {
                            Logger.log(tag, "Полученные уведомления успешно подтверждены на сервере");
                            break;
                        }

                        case "cas600":
                        {
                            Logger.log(tag, "Авторизация прошла успешно");

                            appData.token = serverResponse.data.token;
                            appData.refreshToken = serverResponse.data.refreshToken;
                            appData.session = serverResponse.data.session;

                            Logger.log(tag, "Получение обновлений. . .");
                            RequestObject ro = new RequestObject(Constants.SERVER_SERVICE_NOTIFICATION_CONTROLLER, Constants.SERVER_METHOD_GET_CUSTOM_NOTIFICATION);
                            ro.buildGetNotificationsRequest(appData);
                            try
                            {
                                sendPost(Constants.SERVER_API_URL, gson.toJson(ro, RequestObject.class));
                            }
                            catch (Exception e)
                            {
                                Logger.exc(tag, "Ошибка получения обновлений");
                                Logger.exc(tag, e.toString());
                            }

                            AppData.writeToStorage(context, appData, tag);

                            break;
                        }

                        case "cas400":
                        {
                            Logger.log(tag, "Токен успешно обновлен");
                            appData.refreshToken = serverResponse.data.refreshToken;
                            appData.token = serverResponse.data.token;
                            AppData.writeToStorage(context, appData, tag);
                            break;
                        }

                        case "s151":
                        {
                            Logger.log(tag, "Ошибка авторизации");
                            appData.isOutdated = true;
                            AppData.writeToStorage(context, appData, tag);
                            break;
                        }
                    }
                }
                catch (IOException e)
                {
                    Log.d(Constants.TAG, tag + "IOException: " + e);
                    e.printStackTrace();
                }
            }
        });
    }

    public void showNotification(Context context, String title, String text, CharSequence tickerText, int id, String request)
    {
        Logger.log(tag, "Показывается уведомление " + id);

        Intent notificationIntent = null;
        PendingIntent contentIntent = null;

        //Resources.listResources(context.getPackageName());
        //Log.d(Constants.TAG, tag + "Resource id: " + Resources.getResourseIdByName(context.getPackageName(), Constants.RESOURCE_DRAWABLE, Constants.RESOURCE_ICON_NOTIFICATION));
        int smallIcon = Resources.getResourseIdByName(context.getPackageName(), Constants.RESOURCE_DRAWABLE, Constants.RESOURCE_ICON_NOTIFICATION);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), Resources.getResourseIdByName(context.getPackageName(), Constants.RESOURCE_DRAWABLE, Constants.RESOURCE_ICON_NOTIFICATION));
        largeIcon = Bitmap.createScaledBitmap(largeIcon, largeIcon.getWidth() / 2, largeIcon.getHeight() / 2, true);

        NotificationCompat.Builder builder = null;
        Notification notification = null;

        notificationIntent = new Intent(context, NotifierActivity.class);
        notificationIntent.putExtra(Constants.EXTRA_REQUEST, request);
        contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(context).setContentText(text).setContentTitle(title).setTicker(tickerText).setWhen(System.currentTimeMillis()).setSmallIcon(smallIcon).setLargeIcon(largeIcon).setLights(Color.BLUE, 500, 500).setAutoCancel(true).setContentIntent(contentIntent);

        if (Build.VERSION.SDK_INT <= 15)
        {
            notification = builder.getNotification();
        }
        else
        {
            notification = builder.build();
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }
}