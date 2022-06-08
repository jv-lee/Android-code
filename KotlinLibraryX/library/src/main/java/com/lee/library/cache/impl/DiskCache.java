package com.lee.library.cache.impl;


import com.lee.library.cache.core.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 磁盘缓存实现类
 *
 * @author jv.lee
 * @date 2019-11-14
 */
public class DiskCache {

    /**
     * 磁盘缓存
     */
    private DiskLruCache diskLruCache;

    public DiskCache(String path, int version, int count, long maxSize) {
        try {
            File file = new File(path);
            diskLruCache = DiskLruCache.open(file, version, count, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void put(String key, String value) {
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

    public synchronized String get(String key) {
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


    private String readStreamToString(InputStream inputStream) throws IOException {
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
