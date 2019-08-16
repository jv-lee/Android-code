package com.gionee.gnservice.common.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.gionee.gnservice.utils.LogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by caocong on 3/3/17.
 */

public class UnlimitedDiskCache implements ICacheHelper {
    private static final String TAG = UnlimitedDiskCache.class.getSimpleName();
    private File mCacheDir;


    public UnlimitedDiskCache(File mCacheDir) {
        if (mCacheDir == null) {
            throw new IllegalArgumentException("cacheDir argument must be not null");
        }
        this.mCacheDir = mCacheDir;
    }

    @SuppressWarnings("unused")
    public File getDirectory() {
        return this.mCacheDir;
    }

    private File getCacheFile(String key) {
        return this.getFile(generatorKey(key));
    }


    @Override
    public void clear() {
        File[] files = this.mCacheDir.listFiles();
        boolean success = true;
        if (files != null) {
            File[] arr$ = files;
            int len$ = files.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                File f = arr$[i$];
                success = success && f.delete();
            }
        }
        LogUtil.d(TAG, "clear is success:" + success);

    }

    protected File getFile(String key) {
        File dir = this.mCacheDir;
        return new File(dir, generatorKey(key));
    }


    private String generatorKey(String key) {
        return key;
    }


    @Override
    public boolean put(String key, String value) {
        try {
            return put(generatorKey(key), value.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean put(String key, byte[] data) {
        File imageFile = this.getFile(generatorKey(key));
        File tmpFile = new File(imageFile.getAbsolutePath() + ".tmp");
        boolean success = false;
        BufferedOutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(tmpFile));
            os.write(data);
            os.flush();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
            success = false;
        } finally {
            closeIO(os);
            if (success && !tmpFile.renameTo(imageFile)) {
                success = false;
            }
            if (!success) {
                success = tmpFile.delete();
            }

        }
        return success;
    }

    @Override
    public boolean put(String key, Serializable value) {
        File imageFile = this.getFile(generatorKey(key));
        File tmpFile = new File(imageFile.getAbsolutePath() + ".tmp");
        boolean success = false;
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(new FileOutputStream(tmpFile));
            os.writeObject(value);
            os.flush();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            success = false;
        } finally {
            closeIO(os);
            if (success && !tmpFile.renameTo(imageFile)) {
                success = false;
            }
            if (!success) {
                success = tmpFile.delete();
            }
        }
        return success;
    }

    @Override
    public boolean put(String key, Bitmap bitmap) {
        return put(generatorKey(key), bitmap2Bytes(bitmap));
    }

    private byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private Bitmap bytes2Bitmap(byte[] b) {
        if (b != null && b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    @Override
    public String getString(String key) {
        try {
            byte[] bytes = getByteArray(generatorKey(key));
            if (bytes == null || bytes.length == 0) {
                return "";
            }
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            return "";
        }
    }

    @Override
    public byte[] getByteArray(String key) {
        BufferedInputStream bis = null;
        try {
            File cacheFile = getCacheFile(generatorKey(key));
            if (cacheFile == null || !cacheFile.exists()) {
                return null;
            }
            bis = new BufferedInputStream(new FileInputStream(cacheFile));
            byte[] byteArray = new byte[(int) cacheFile.length()];
            int result = bis.read(byteArray);
            Log.d(TAG, "get byte array result is:" + result);
            return (result == -1) ? null : byteArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeIO(bis);
        }
    }

    @Override
    public Object getObject(String key) {
        ObjectInputStream ois = null;
        try {
            File cacheFile = getCacheFile(generatorKey(key));
            if (cacheFile == null || !cacheFile.exists()) {
                return null;
            }
            ois = new ObjectInputStream(new FileInputStream(cacheFile));
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeIO(ois);
        }
    }

    @Override
    public Bitmap getBitmap(String key) {
        byte[] bytes = getByteArray(generatorKey(key));
        return bytes2Bitmap(bytes);
    }

    @Override
    public boolean isExpired(String key, IExpiredStrategy expiredStrategy) {
        File cacheFile = getCacheFile(generatorKey(key));
        boolean isExpired = expiredStrategy.isExpired(cacheFile.lastModified());
        Log.d(TAG, "get cache key is:" + generatorKey(key) + "; is expired is:" + isExpired);
        return isExpired;
    }


    private void closeIO(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean remove(String key) {
        return this.getFile(generatorKey(key)).delete();
    }


}
