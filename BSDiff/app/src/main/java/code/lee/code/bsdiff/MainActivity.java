package code.lee.code.bsdiff;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import code.lee.code.bsdiff.utils.UriParseUtils;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {
    private Activity activity;

    //用于在应用程序启东时 直接加载本地lib
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        String version = null;
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(version);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                update();
                UriParseUtils.installApk(activity,new File(Environment.getExternalStorageDirectory(),"bsdiff.apk"));
            }
        });

        //运行时权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms,200);
            }
        }
    }

    /**
     *
     * @param oldApk 旧版本安装包 如1.1.1 版本的路径
     * @param patch 差分包，patch文件
     * @param output 合成之后的新文件，新版本apk的输出/保存路径
     */
    public native String bsPatch(String oldApk, String patch, String output);


    @SuppressLint("StaticFieldLeak")
    public void update(){
        new AsyncTask<Void, Void, File>() {

            @Override
            protected File doInBackground(Void... voids) {
                String oldApk = getApplicationInfo().sourceDir;
                String patch = new File(Environment.getExternalStorageDirectory(), "patch").getAbsolutePath();
                String output = createNewApk();
                bsPatch(oldApk, patch, output);
                return new File(output);
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                UriParseUtils.installApk(activity,file);
            }
        }.execute();
    }

    /**
     * 创建合成后的新版本apk文件
     * 提前占坑
     * @return
     */
    private String createNewApk() {
        File newApk = new File(Environment.getExternalStorageDirectory(), "bsdiff.apk");
        if (!newApk.exists()) {
            try {
                boolean newFile = newApk.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newApk.getAbsolutePath();
    }
}
