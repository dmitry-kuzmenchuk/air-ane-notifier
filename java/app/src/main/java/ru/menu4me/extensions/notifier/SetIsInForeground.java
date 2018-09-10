package ru.menu4me.extensions.notifier;

import android.app.NativeActivity;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FREObject;
import com.adobe.fre.FRETypeMismatchException;
import com.adobe.fre.FREWrongThreadException;

/**
 * Use to set application state inside the adobe air project in events Activate and Deactivate
 */
public class SetIsInForeground implements FREFunction {
    private final String tag = "Set Is In Foreground";

    @Override
    public FREObject call(FREContext context, FREObject[] args) {
        Logger.log(tag, "Установка переменной isInForeground");

        Boolean isInForeground = null;
        try {
            isInForeground = args[0].getAsBool();
        } catch (FRETypeMismatchException e) {
            e.printStackTrace();
        } catch (FREInvalidObjectException e) {
            e.printStackTrace();
        } catch (FREWrongThreadException e) {
            e.printStackTrace();
        }

        if (isInForeground == null) {
            Logger.exc(tag, "Некорректное значение аргумента isInForeground");
        } else {
            Extension.isInForeground = isInForeground;
            Logger.log(tag, "isInForeground = " + isInForeground.toString());
        }

        return null;
    }
}
