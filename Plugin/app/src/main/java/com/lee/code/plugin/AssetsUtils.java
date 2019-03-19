package com.lee.code.plugin;

import android.app.Activity;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AssetsUtils {
    public static String copyAssetAndWrite(Activity activity, String fileName) {

        try {
            File cacheDir = activity.getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File outFile = new File(cacheDir, fileName);
            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (res) {
                    InputStream is = activity.getAssets().open(fileName);
                    FileOutputStream fos = new FileOutputStream(outFile);
                    byte[] buffer = new byte[is.available()];
                    int byteCount;
                    while ((byteCount = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, byteCount);
                    }
                    fos.flush();
                    is.close();
                    fos.close();
                    Toast.makeText(activity, "下载成功", Toast.LENGTH_SHORT).show();
                    return outFile.getAbsolutePath();
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
