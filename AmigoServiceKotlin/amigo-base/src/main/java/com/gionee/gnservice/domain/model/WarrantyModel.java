package com.gionee.gnservice.domain.model;

import android.text.TextUtils;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.cache.ICacheHelper;
import com.gionee.gnservice.common.http.HttpParam;
import com.gionee.gnservice.common.http.IHttpHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.domain.Observable;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PhoneUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class WarrantyModel extends BaseModel {
    private static final String TAG = WarrantyModel.class.getSimpleName();
    private Cache mCache;

    public WarrantyModel(IAppContext appContext) {
        super(appContext);
        mCache = new Cache(appContext);
    }

    public Observable<Integer> getWarranty(final String token) {
        return new Observable<Integer>() {
            @Override
            protected Object doInBackground(Object... p) {
                try {
                    IHttpHelper httpHelper = mAppContext.httpHelper();
                    Map<String, String> params = new LinkedHashMap<String, String>();
                    params.put("token", token);
                    params.put("imei", PhoneUtil.getIMEI(mAppContext.application()));

                    HttpParam.Builder builder = new HttpParam.Builder();
                    builder.setUrl(AppConfig.URL.URL_GET_YAN_BAO).setParams(params);
                    String responseInfo = httpHelper.post(builder.build()).getString();
                    LogUtil.d(TAG, "load warranty content=" + responseInfo);
                    if (TextUtils.isEmpty(responseInfo)) {
                        publishError(new Exception("load warranty fail"));
                        return null;
                    }
                    String result = parseJson(responseInfo);
                    mCache.updateWarrentyYearCache(result);
                    publishNext(Integer.parseInt(result));

                } catch (Exception e) {
                    e.printStackTrace();
                    publishError(e);
                }
                return null;
            }
        };
    }

    // json format:{"enddate":1533172227867,"err":"0","msg":"imei延保未申请","startdate":1501722627867,"userRegisterCount":0}
    private String parseJson(String json) throws JSONException {
        if (TextUtils.isEmpty(json)) {
            LogUtil.e(TAG, "json is empty, return default value");
            return "1";
        }

        try {
            JSONObject result = new JSONObject(json);
            int errCode = result.getInt("err");
            if (errCode != 0) {
                LogUtil.i(TAG, "server response errCode = " + errCode);
            } else {
                long endDate = result.getLong("enddate");
                long startDate = result.getLong("startdate");
                float a = (endDate - startDate) / (365 * 24 * 3600 * 1000f);
                int value = Math.round(a);
                if (value > 0 && value < 4) {
                    return String.valueOf(value);
                }
            }
        } catch (JSONException e) {
            LogUtil.e(TAG, "parse json error, json: " + json);
        } catch (Exception e) {
            LogUtil.e(TAG, "parse error, msg: " + e.getMessage());
        }
        return "1"; //return default 1 year
    }

    private static class Cache {
        private static final String CACHE_WARRANTY_YEAR = "cache_warranty_year";
        private ICacheHelper mCacheHelper;

        public Cache(IAppContext appContext) {
            mCacheHelper = appContext.cacheHelper();
        }

        public String getWarrantyYearCache() {
            return mCacheHelper.getString(CACHE_WARRANTY_YEAR);
        }

        public void updateWarrentyYearCache(String year) {
            LogUtil.d(TAG, "updateWarrentyYearCache year=" + year);
            mCacheHelper.put(CACHE_WARRANTY_YEAR, year);
        }
    }
}
