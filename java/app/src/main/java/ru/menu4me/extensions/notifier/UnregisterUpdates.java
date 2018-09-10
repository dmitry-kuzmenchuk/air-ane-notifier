package ru.menu4me.extensions.notifier;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

public class UnregisterUpdates implements FREFunction {
    private static final String tag = "Unregister Updates";

    @Override
    public FREObject call(FREContext context, FREObject[] args) {
        Logger.log(tag, "Отмена обновлений. . .");
        Context appContext = context.getActivity().getApplicationContext();

        Intent alarmIntent = new Intent();
        alarmIntent.setAction(Constants.ACTION_UPDATE);
        alarmIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(appContext, 0, alarmIntent, 0);

        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Logger.log(tag, "Обновления успешно отменены");
        return null;
    }
}
