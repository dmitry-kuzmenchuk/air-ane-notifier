package ru.menu4me.extensions.notifier;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;

import java.util.HashMap;
import java.util.Map;

public class ExtensionContext extends FREContext {
    private static final String tag = "Extension Context";

    @Override
    public Map<String, FREFunction> getFunctions() {
        Logger.log(tag, "Получение функций, используемых расширением");
        Map<String, FREFunction> functions = new HashMap<>();
        functions.put("registerUpdates", new RegisterUpdates());
        functions.put("unregisterUpdates", new UnregisterUpdates());
        functions.put("fetchStarterNotification", new FetchStarterNotification());
        functions.put("setIsInForeground", new SetIsInForeground());
        functions.put("initData", new InitData());
        functions.put("forceUpdate", new ForceUpdate());
        functions.put("purgeData", new PurgeData());
        return functions;
    }

    @Override
    public void dispose() {
        Log.d(Constants.TAG, tag + "dispose()");
        Extension.context = null;
    }
}
