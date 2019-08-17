package com.lee.app.utils;

import java.security.MessageDigest;

/**
 * @author ?
 */
public class MD5Util {

    /**
     * md5
     *
     * @param plaintext
     * @return
     * @throws
     */
    public static String getMD5(String plaintext) throws Exception {
        String s;
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(plaintext.getBytes("UTF-8"));
        byte[] tmp = md.digest();
        char[] str = new char[16 * 2];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte byte0 = tmp[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        s = new String(str);
        return s;
    }
}
