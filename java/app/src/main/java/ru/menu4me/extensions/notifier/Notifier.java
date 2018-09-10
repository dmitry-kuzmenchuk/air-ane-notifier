package ru.menu4me.extensions.notifier;

import android.app.IntentService;
import android.content.Intent;

public class Notifier extends IntentService {

    private String mThreadName;

    public Notifier(String threadName) {
        super(threadName);
        mThreadName = threadName;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
