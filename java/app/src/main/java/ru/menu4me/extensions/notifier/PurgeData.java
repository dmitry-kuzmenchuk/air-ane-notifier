package ru.menu4me.extensions.notifier;


import android.content.Context;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

public class PurgeData implements FREFunction {
    private static final String tag = "Purge Data";

    @Override
    public FREObject call(FREContext context, FREObject[] args) {
        Log.d(Constants.TAG, tag + "call()");
        Context appContext = context.getActivity().getApplicationContext();
        AppData.purge(appContext);
        return null;
    }
}
