package ru.menu4me.extensions.notifier;

import android.content.Context;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FREObject;
import com.adobe.fre.FRETypeMismatchException;
import com.adobe.fre.FREWrongThreadException;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

public class InitData implements FREFunction {
    private static final String tag = "Init Data";

    /**
     * Аргумент 1 = login (string)
     * Аргумент 2 = password (string)
     * Аргумент 3 = clientInfo (string)
     * Аргумент 4 = owner.id (int)
     */
    @Override
    public FREObject call(FREContext context, FREObject[] args) {
        Logger.log(tag, "Инициализация данных приложения в расширении");
        Context appContext = context.getActivity().getApplicationContext();

        try {
            File dataFile = new File(appContext.getFilesDir(), Constants.DATA_FILENAME);
            AppData data = AppData.readFromStorage(appContext, tag);

            if (!dataFile.exists()) data = new AppData();
            data.login = args[0].getAsString();
            data.password = args[1].getAsString();
            data.clientInfo = args[2].getAsString();
            data.ownerId = args[3].getAsString();

            AppData.writeToStorage(appContext, data, tag);
        } catch (FREWrongThreadException e) {
            e.printStackTrace();
        } catch (FREInvalidObjectException e) {
            e.printStackTrace();
        } catch (FRETypeMismatchException e) {
            e.printStackTrace();
        }

        Logger.log(tag, "Данные успешно инициализированы");
        return null;
    }
}
