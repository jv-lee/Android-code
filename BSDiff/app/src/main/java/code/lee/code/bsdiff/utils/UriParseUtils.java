package code.lee.code.bsdiff.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

public class UriParseUtils {

    /**
     * 创建一个文件输出路径的Uri (FileProvider)
     *
     * @param context 上下文
     * @param file    文件
     * @return 转换后的Scheme为FileProvider的Uri
     */
    private static Uri getUriForFile(Context context, File file) {
        return FileProvider.getUriForFile(context, getFileProvider(context), file);
    }

    /**
     * 获取FileProvider路径，适配6.0+
     *
     * @param context 上下文
     * @return FileProvider路径
     */
    private static String getFileProvider(Context context) {
        return context.getApplicationInfo().packageName + ".fileprovider";
    }

    /**
     * 安装apk
     */
    public static void installApk(Activity activity, File apkfile) {
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Uri fileUri = getUriForFile(activity, apkfile);
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        }

        activity.startActivity(intent);
    }

}
