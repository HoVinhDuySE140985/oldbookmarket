package com.example.oldbookmarket.shared.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Utilities {
    public static String calculateHMac(String data, String algorithm, String key) throws Exception {
        Mac Hmac = Mac.getInstance(algorithm);
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
        Hmac.init(secret_key);

        return byteArrayToHex(Hmac.doFinal(data.getBytes("UTF-8")));
  
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
              sb.append(String.format("%02x", b));
        return sb.toString();
  }

}
