package ru.menu4me.extensions.notifier;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.SystemClock;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

public class RegisterUpdates implements FREFunction {
    private static final String tag = "Register Updates";

    @Override
    public FREObject call(FREContext context, FREObject[] args) {
        Logger.log(tag, "Регистрация обновлений. . .");
        Context appContext = context.getActivity().getApplicationContext();
        register(appContext);
        return null;
    }

    /**
     * Регистрирует будильник с задержкой в 1 минуту,
     * который отправляет на наш ресивер Intent
     * @param context
     */
    public static void register(Context context) {
        Intent actionIntent = new Intent(context, NotifierBroadcastReceiver.class);
        actionIntent.setAction(Constants.ACTION_UPDATE);
        actionIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + Constants.PERIOD_MINUTE, Constants.PERIOD_MINUTE, alarmIntent);
        Logger.log(tag, "Обновления успешно зарегистрированы");
    }
}
