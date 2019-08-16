package com.gionee.gnservice.domain.model;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.http.HttpParam;
import com.gionee.gnservice.common.http.IHttpHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.entity.MemberLevel;
import com.gionee.gnservice.exception.NetWorkException;
import com.gionee.gnservice.utils.LogUtil;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caocong on 6/27/17.
 */
public class MemberCardImgModel extends BaseModel {
    private static final String TAG = MemberCardImgModel.class.getSimpleName();
    private static final String UNLOGIN = "u";
    private static final String LOGIN_FRONT = "f";
    private static final String LOGIN_BACK = "b";
    private static HashMap<MemberLevel, List<Bitmap>> mMemoryCache = new HashMap<MemberLevel, List<Bitmap>>();
    private String mCacheFileDir;

    public MemberCardImgModel(IAppContext appContext) {
        super(appContext);
        initCacheDir(appContext.application());
    }

    private void initCacheDir(Context context) {
        mCacheFileDir = context.getCacheDir().getPath();
    }

    public static boolean hasPermission(Context context, String permission) {
        int perm = context.checkCallingOrSelfPermission(permission);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    public com.gionee.gnservice.domain.Observable<List<Bitmap>> getMemberCardImg(final MemberLevel level) {
        return new com.gionee.gnservice.domain.Observable<List<Bitmap>>() {
            @Override
            protected Object doInBackground(Object... params) {
                List<Bitmap> cacheBitmaps = mMemoryCache.get(level);
                if (cacheBitmaps != null && cacheBitmaps.size() == 3) {
                    LogUtil.d(TAG, "get cache bitmap from memory");
                    publishNext(cacheBitmaps);
                    return null;
                }
                cacheBitmaps = getDiskCacheBitmaps(level);
                if (cacheBitmaps != null && cacheBitmaps.size() == 3) {
                    LogUtil.d(TAG, "get cache bitmap from disk");
                    publishNext(cacheBitmaps);
                    return null;
                }
                LogUtil.d(TAG, "have no cache bitmap ,get bitmap from net");
                try {
                    String urlJson = loadImgUrlFromNet(level);
                    List<String> urlList = parseImgUrlJson(urlJson, level);
                    List<Bitmap> bitmaps = new ArrayList<Bitmap>();
                    for (String imgUrl : urlList) {
                        Bitmap bitmap = loadBitmapFromNet(imgUrl);
                        if (bitmap == null) {
                            break;
                        }
                        bitmaps.add(bitmap);
                    }
                    if (bitmaps.size() == 3) {
                        publishNext(bitmaps);
                        mMemoryCache.put(level, bitmaps);
                        cacheBitmap2Disk(bitmaps, level);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    publishError(new NetWorkException());
                }
                return null;
            }
        };
    }

    private String loadImgUrlFromNet(MemberLevel level) throws Exception {
        LogUtil.d(TAG, "load member card image from net");
        IHttpHelper httpHelper = mAppContext.httpHelper();
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("k", "ucs");
        params.put("m", Build.MODEL);
        StringBuffer sb = new StringBuffer();
        sb.append(UNLOGIN + level.getValue());
        sb.append("-");
        sb.append(LOGIN_FRONT + level.getValue());
        sb.append("-");
        sb.append(LOGIN_BACK + level.getValue());
        params.put("ext", sb.toString());
        HttpParam.Builder builder = new HttpParam.Builder();
        builder.setUrl(AppConfig.URL.getMemberCardViewImageUrl()).setParams(params);
        return httpHelper.get(builder.build()).getString();
    }

    private List<String> parseImgUrlJson(String json, MemberLevel level) throws Exception {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        List<String> imgUrlList = new ArrayList<String>();

        JSONObject jos = new JSONObject(json);
        String imgUrlUnLogin = jos.getString(UNLOGIN + level.getValue());
        String imgUrlLoginFront = jos.getString(LOGIN_FRONT + level.getValue());
        String imgUrlLoginBack = jos.getString(LOGIN_BACK + level.getValue());

        imgUrlList.add(imgUrlUnLogin);
        imgUrlList.add(imgUrlLoginFront);
        imgUrlList.add(imgUrlLoginBack);
        return imgUrlList;
    }

    private Bitmap loadBitmapFromNet(String imgUrl) throws Exception {
        LogUtil.d(TAG, "load member card image from net");
        IHttpHelper httpHelper = mAppContext.httpHelper();
        HttpParam.Builder builder = new HttpParam.Builder();
        builder.setUrl(imgUrl);
        byte[] data = httpHelper.get(builder.build()).getBytes();
        Bitmap bitmap = null;
        if (data != null) {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        LogUtil.d(TAG, "load bitmap success!");
        return bitmap;
    }

    private void cacheBitmap2Disk(List<Bitmap> bitmaps, MemberLevel level) throws Exception {
        LogUtil.d(TAG, "cache bitmap");
        int levelValue = level.getValue();
        for (int i = 0; i < bitmaps.size(); i++) {
            Bitmap bitmap = bitmaps.get(i);
            String cacheName = "";
            if (i == 0) {
                cacheName = UNLOGIN + levelValue;
            } else if (i == 1) {
                cacheName = LOGIN_FRONT + levelValue;
            } else {
                cacheName = LOGIN_BACK + levelValue;
            }
            LogUtil.d(TAG, "cache bitmap to disk,name is:" + cacheName);

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(mCacheFileDir, cacheName)));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        }
    }

    private List<Bitmap> getDiskCacheBitmaps(MemberLevel level) {
        LogUtil.d(TAG, "get disk cache bitmap");
        int levelValue = level.getValue();
        List<String> imgPathList = new ArrayList<String>();
        imgPathList.add(new File(mCacheFileDir, UNLOGIN + levelValue).getPath());
        imgPathList.add(new File(mCacheFileDir, LOGIN_FRONT + levelValue).getPath());
        imgPathList.add(new File(mCacheFileDir, LOGIN_BACK + levelValue).getPath());

        List<Bitmap> cacheBitmap = new ArrayList<Bitmap>();
        for (String path : imgPathList) {
            LogUtil.d(TAG, "get bitmap path is:" + path);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            LogUtil.d(TAG, "get bitmap is null:" + (bitmap == null));
            if (bitmap == null) {
                break;
            }
            cacheBitmap.add(bitmap);
        }
        return cacheBitmap;
    }
}
