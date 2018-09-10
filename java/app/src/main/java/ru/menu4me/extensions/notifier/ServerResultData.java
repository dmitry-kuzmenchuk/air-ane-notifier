package ru.menu4me.extensions.notifier;

import java.util.ArrayList;

public class ServerResultData {
    public String message = null;
    public String token = null;
    public String refreshToken = null;
    public String session = null;
    public ArrayList<NotificationData> items = new ArrayList<NotificationData>();

    public ServerResultData() {

    }
}
