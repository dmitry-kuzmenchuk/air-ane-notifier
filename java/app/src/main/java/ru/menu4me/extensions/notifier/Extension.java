package ru.menu4me.extensions.notifier;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;

public class Extension implements FREExtension {
    private String tag = "Extension ";
    public static FREContext context;
    public static Boolean isInForeground = false;

    @Override
    public FREContext createContext(String contextType) {
        Log.d(Constants.TAG, tag + "createContext()");
        return context = new ExtensionContext();
    }

    @Override
    public void initialize() {
        Log.d(Constants.TAG, tag + "initialize()");
    }

    @Override
    public void dispose() {
        Log.d(Constants.TAG, tag + "dispose()");
    }
}
