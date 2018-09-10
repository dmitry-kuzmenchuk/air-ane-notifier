package ru.menu4me.extensions.notifier;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

/**
 * Use to fetch event and request from NotifierActivity, which are broker of notification data
 * that we are have got recently
 */
public class FetchStarterNotification implements FREFunction {
    private String tag = "Fetch Starter Notification";

    @Override
    public FREObject call(FREContext context, FREObject[] args) {
        Logger.log(tag, "Получение стартового push-уведомления");

        // If NotifierActivity wasn't initialized properly (application was launched first time),
        // init isFromNotification static variable by ourselves to avoid any errors at runtime
        if (NotifierActivity.isFromNotification == null) {
            /*Log.e(Constants.TAG, tag + "called before NotifierActivity was initialized," +
                    "initializing isFromNotification static value as false to avoid errors at runtime");*/
            NotifierActivity.isFromNotification = false;
        }

        if (NotifierActivity.isFromNotification) {
            String request = NotifierActivity.mRequest;
            request = request == null ? "" : request;
            Logger.log(tag, "Приложение запущено через push-уведомление, обрабатываем событие и получаем его данные: " + request);
            context.dispatchStatusEventAsync(Constants.EVENT_FROM_NOTIFICATION, request);
        } else {
            //Log.d(Constants.TAG, tag + "isFromNotification are false, so we are don't have to dispatch any event");
        }

        return null;
    }
}
