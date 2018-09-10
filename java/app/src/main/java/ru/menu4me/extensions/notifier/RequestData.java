package ru.menu4me.extensions.notifier;

import java.util.ArrayList;

public class RequestData {
    public String email = null;
    public String time = null;
    public String clientInfo = null;
    public String signature = null;
    public String token = null;
    public String refreshToken = null;
    public int documentType;
    public OwnerData owner;
    public int[] notifications;
}