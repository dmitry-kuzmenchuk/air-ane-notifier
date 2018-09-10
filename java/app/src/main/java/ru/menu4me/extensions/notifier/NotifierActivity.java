package ru.menu4me.extensions.notifier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Broker of notification data, gets data when notification getting activated and keeps it at static variables
 */
public class NotifierActivity extends Activity {
    private String tag = "Notifier Activity ";

    public static String mRequest;
    public static Boolean isFromNotification;
    //public static Boolean isInForeground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Constants.TAG, tag + "onCreate()");
        super.onCreate(savedInstanceState);
        isFromNotification = false;

        String request = getIntent().getStringExtra(Constants.EXTRA_REQUEST);
        if (request != null) {
            isFromNotification = true;
            mRequest = request;

            Log.d(Constants.TAG, tag + "Dispatching event with request: " + request);

            // If application are running and in foreground - dispatch EVENT_FOREGROUND_NOTIFICATION
            // If application are running and in background - dispatch EVENT_FROM_NOTIFICATION
            // If application are not running - use fetchStarterNotification to dispatch EVENT_FROM_NOTIFICATION
            if (Extension.context != null) {
                if (!Extension.isInForeground){
                    Extension.context.dispatchStatusEventAsync(Constants.EVENT_FROM_NOTIFICATION, request);
                }
                isFromNotification = false;
            }
        }

        try {
            Intent intent = new Intent(this, Class.forName(getPackageName() + ".AppEntry"));
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            Log.e(Constants.TAG, tag + "ClassNotFoundException: " + e);
            e.printStackTrace();
        }
        finish();
    }
}