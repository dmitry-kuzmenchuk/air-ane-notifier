package ru.menu4me.extensions.notifier;

import java.util.ArrayList;
import java.util.Random;

public class RequestObject {
    public String service = "";
    public String method = "";
    public RequestData data = null;

    public RequestObject(String service, String method) {
        this.service = service;
        this.method = method;
    }

    public void buildAuthRequest(AppData appData) {
        this.data = new RequestData();
        this.data.email = appData.login;
        this.data.clientInfo = appData.clientInfo;
        generateSignature(appData);
    }

    public void buildRefreshTokenRequest(AppData appData) {
        this.data = new RequestData();
        this.data.token = appData.token;
        this.data.refreshToken = appData.refreshToken;
        generateSignature(appData);
    }

    public void buildGetNotificationsRequest(AppData appData) {
        this.data = new RequestData();
        this.data.token = appData.token;
        this.data.documentType = 384;
        this.data.clientInfo = appData.clientInfo;
        this.data.owner = new OwnerData();
        this.data.owner.id = appData.ownerId.split(",");
        generateSignature(appData);
    }

    public void buildSetNotificationReceivedRequest(AppData appData, int[] items) {
        this.data = new RequestData();
        this.data.documentType = 384;
        this.data.clientInfo = appData.clientInfo;
        this.data.token = appData.token;
        this.data.owner = new OwnerData();
        this.data.owner.id = appData.ownerId.split(",");
        this.data.notifications = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            this.data.notifications[i] = items[i];
        }
        generateSignature(appData);
    }

    public void generateSignature(AppData data) {
        Random rd = new Random();
        short millis = 0;
        long currentTime = System.currentTimeMillis();
        while (millis < 100) {
            millis = (short) rd.nextInt();
        }
        try {
            this.data.time = String.valueOf(currentTime) + "." + String.valueOf(millis).substring(0, 3);
            this.data.signature = SHA256.getSHA(this.service + this.method + this.data.time + data.token + SHA256.getSHA(data.login) + data.session + SHA256.getSHA(data.password));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
