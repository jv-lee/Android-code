package com.lee.library.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author jv.lee
 * @date 2019/7/23.
 */
public class RawUtil {

    /**
     * 读取raw json配置文件
     *
     * @param context
     * @param rawID
     * @return
     */
    public static String readRawJson(Context context, int rawID) {
        InputStream inputStream = context.getResources().openRawResource(rawID);
        return getString(inputStream);
    }

    private static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
