package ru.menu4me.extensions.notifier;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

public class AppData {
    public String login = "";
    public String password = "";
    public String clientInfo = "";
    public String token = "";
    public String refreshToken = "";
    public String session = "";
    public String ownerId = "";
    public OwnerData owner = null;
    public boolean isOutdated = false;

    /**
     * Считать данные из data-файла, находящегося в памяти устройства
     */
    public static AppData readFromStorage(Context context, String tag)
    {
        Gson gson = new Gson();
        AppData appData;

        try
        {
            File file = new File(context.getFilesDir(), Constants.DATA_FILENAME);
            if (!file.exists())
            {
                Logger.exc(tag, "Файла с данными не существует, ");
                return null;
            }
            else
            {
                Logger.log(tag, "Файл с данными найден, считываю. . .");
                BufferedReader br = new BufferedReader(new FileReader(file));
                String result = br.readLine();
                br.close();

                appData = gson.fromJson(result, AppData.class);
                Logger.log(tag, "Полученные данные:");
                Logger.log(tag, gson.toJson(appData));
            }
        }
        catch (IOException e)
        {
            Logger.exc(tag, "Ошибка чтения данных");
            Logger.exc(tag, e.toString());
            return null;
        }

        return appData;
    }

    public static void writeToStorage(Context context, AppData appData, String tag)
    {
        Gson gson = new Gson();
        String data = gson.toJson(appData);

        try
        {
            Logger.log(tag, "Запись данных в " + Constants.DATA_FILENAME);
            Logger.log(tag, "Записываемые данные: " + data);

            OutputStream os = context.openFileOutput(Constants.DATA_FILENAME, Context.MODE_PRIVATE);
            os.write(data.getBytes());
            os.close();
        }
        catch (IOException e)
        {
            Logger.exc(tag, "Ошибка записи данных в " + Constants.DATA_FILENAME);
            Logger.log(tag, e.toString());
        }

        Logger.log(tag, "Запись данных успешна");
    }

    public static void purge(Context context)
    {
        Logger.log("AppData", "Очистка данных. . .");
        File file = new File(context.getFilesDir(), Constants.DATA_FILENAME);
        if (file.exists())
        {
            file.delete();
            Logger.log("AppData", "Данные очищены");
        }
        else
        {
            Logger.log("AppData", "Нет данных для очистки, мы чисты!");
        }
    }
}
