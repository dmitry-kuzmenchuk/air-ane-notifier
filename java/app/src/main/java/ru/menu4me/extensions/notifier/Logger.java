package ru.menu4me.extensions.notifier;

import android.util.Log;

public class Logger
{
    public static void log(String tag, String text)
    {
        Log.d(Constants.TAG, "(" + tag + ") " + text);
    }

    public static void exc(String tag, String exception)
    {
        Log.e(Constants.TAG, "(" + tag + ") " + exception);
    }
}
