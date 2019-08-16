package com.gionee.gnservice.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

/**
 * Created by caocong on 8/1/17.
 */
public class ImageOOMUtil {
    private static final String TAG = ImageOOMUtil.class.getSimpleName();
    private static final boolean DEBUG = false;
    private static LruCache<Integer, Bitmap> sLruCache;

    static {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/6th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 6;

        sLruCache = new LruCache<Integer, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(Integer key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }

            @Override
            protected void entryRemoved(boolean evicted, Integer key, Bitmap oldValue, Bitmap newValue) {
//                    recycleBitmap(sLruCache.remove(key));
            }
        };
    }

    public static Bitmap getBitmap(Context context, int drawableId) {
        if (sLruCache != null) {
            Bitmap bitmap = sLruCache.get(drawableId);
            if (bitmap != null) {
                if (DEBUG) LogUtil.d(TAG, "get " + drawableId + " from lrucache!!");
                return bitmap;
            }
        }

        Resources res = context.getResources();
        int width = res.getDimensionPixelOffset(ResourceUtil.getDimenId(context, "uc_include_member_view_card_width"));
        int height = res.getDimensionPixelOffset(ResourceUtil.getDimenId(context, "uc_include_member_view_card_height"));

        Bitmap bitmap = decodeSampledBitmapFromResource(res, drawableId, width, height);

        if (sLruCache != null) {
            if (DEBUG) LogUtil.d(TAG, "put " + drawableId + " to lrucache");
            sLruCache.put(drawableId, bitmap);
        }

        return bitmap;
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                          int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        if (DEBUG) LogUtil.i(TAG, "decodeSampledBitmapFromResource-inSampleSize:" + options.inSampleSize);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }
}
