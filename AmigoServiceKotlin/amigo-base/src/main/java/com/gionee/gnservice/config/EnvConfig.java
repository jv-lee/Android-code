package com.gionee.gnservice.config;

import android.content.Context;
import android.os.Environment;

import com.gionee.gnservice.utils.SdkUtil;

import java.io.File;

public class EnvConfig {
    private static final String TAG = EnvConfig.class.getSimpleName();
    private static final String TEST_ENV_DIR = "/service1234567890test";
    private static final String TEST_ENV_SDK_DIR = "/service1234567890sdk";

    private static boolean PRO_ENV = true;
    private static boolean SDK_ENV = true;
    private static boolean sENV = PRO_ENV;

    static {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            String sdDirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File debugFile = new File(sdDirPath + TEST_ENV_DIR);
            PRO_ENV = !debugFile.exists();

            File sdkFile = new File(sdDirPath + TEST_ENV_SDK_DIR);
            SDK_ENV = !sdkFile.exists();
        }
    }

    public static boolean isProEnv() {
        return sENV;
    }

    public static void init(Context context) {
        sENV = SdkUtil.isCallBySdk(context) ? SDK_ENV : PRO_ENV;
    }

}
