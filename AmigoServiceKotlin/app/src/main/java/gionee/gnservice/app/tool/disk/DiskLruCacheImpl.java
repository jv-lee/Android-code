package gionee.gnservice.app.tool.disk;

import android.os.Environment;
import com.gionee.gnservice.BuildConfig;

import java.io.*;

/**
 * @author jv.lee
 * @date 2019-09-01
 * @description 磁盘缓存自身封装类
 */
public class DiskLruCacheImpl {

    /**
     * 磁盘存储地址
     */
    private final String DISK_LRU_CACHE_DIR = "disk_lru_cache_dir";

    /**
     * app版本，每次只缓存当前版本，修改后之前的缓存失效
     */
    private final int APP_VERSION = BuildConfig.VERSION_CODE;

    /**
     * 通常情况下count为1
     */
    private final int VALUE_COUNT = 1;

    /**
     * 最大缓存大小 可动态设置
     */
    private final long MAX_SIZE = 1024 * 1024 * 100;

    /**
     * 磁盘缓存
     */
    private DiskLruCache diskLruCache;

    public DiskLruCacheImpl() {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + DISK_LRU_CACHE_DIR);
            diskLruCache = DiskLruCache.open(file, APP_VERSION, VALUE_COUNT, MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, String value) {
        DiskLruCache.Editor edit = null;
        OutputStream os = null;
        byte[] bytes = value.getBytes();
        try {
            edit = diskLruCache.edit(key);
            //index不能大于VALUE_COUNT
            os = edit.newOutputStream(0);
            if (os != null) {
                os.write(bytes);
                os.flush();
            }
        } catch (IOException e) {
            try {
                if (edit != null) {
                    edit.abort();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (edit != null) {
                    edit.commit();
                }
                diskLruCache.flush();
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String get(String key) {
        DiskLruCache.Snapshot snapshot = null;
        InputStream is = null;
        String content = null;

        try {
            snapshot = diskLruCache.get(key);
            if (snapshot != null) {
                is = snapshot.getInputStream(0);
                content = readStreamToString(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (snapshot != null) {
                    snapshot.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }


    public static String readStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }

        String result = byteArrayOutputStream.toString();
        //关闭输入流和输出流
        inputStream.close();
        byteArrayOutputStream.close();
        //返回字符串结果
        return result;
    }

}
