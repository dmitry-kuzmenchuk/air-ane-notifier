package ru.menu4me.extensions.notifier;

import java.security.MessageDigest;

public class SHA256 {

    public static String getSHA(String text) throws Exception {
        MessageDigest md = MessageDigest.getInstance(Constants.HASH_SHA256);
        md.update(text.getBytes());
        byte[] byteData = md.digest();

        // convert the byte to hex format
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
