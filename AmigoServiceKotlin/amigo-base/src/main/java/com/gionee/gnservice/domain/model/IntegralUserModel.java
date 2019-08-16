package com.gionee.gnservice.domain.model;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.cache.ICacheHelper;
import com.gionee.gnservice.common.http.HttpParam;
import com.gionee.gnservice.common.http.IHttpHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.domain.Observable;
import com.gionee.gnservice.entity.IntegralInterface;
import com.gionee.gnservice.utils.GNDecodeUtils;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.NetworkUtil;
import com.gionee.gnservice.utils.PhoneUtil;
import com.gionee.gnservice.utils.SdkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by caocong on 6/1/17.
 */
public class IntegralUserModel extends BaseModel {
    private static final String TAG = IntegralUserModel.class.getSimpleName();
    private Cache mCache;

    public IntegralUserModel(IAppContext appContext) {
        super(appContext);
        mCache = new Cache(appContext.cacheHelper());
    }

    public Observable<IntegralInterface> getUserIntegral(final String token) {
        return new Observable<IntegralInterface>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    if (!isNetworkConnect()) {
                        LogUtil.i(TAG, "network is not connect or has something wrong, get from cache.");
                        String infoStr = mCache.getCacheIntegral();
                        publishNext(infoStr == null ? new IntegralInterface() : parseJson(infoStr));
                        return null;
                    }
                    String response = loadDataFromNet(token);
                    LogUtil.i(TAG, "get user integral is:" + response);
                    if (TextUtils.isEmpty(response)) {
                        publishNext(new IntegralInterface());
                        return null;
                    }

                    mCache.cacheIntegral(response);
                    publishNext(parseJson(response));
                } catch (Exception e) {
                    e.printStackTrace();
                    publishError(e);
                }
                return null;
            }
        };
    }

    private String loadDataFromNet(String token) throws Exception {
        LogUtil.i(TAG, "load lottery remain times from net");
        IHttpHelper httpHelper = mAppContext.httpHelper();
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("model", Build.MODEL);
        params.put("imei", GNDecodeUtils.get(PhoneUtil.getIMEI(mAppContext.application())));
        params.put("token", Base64.encodeToString(token.getBytes("utf-8"), Base64.DEFAULT));
        params.put("ch", SdkUtil.AMIGO_SERVICE_PACKAGE_NAME);
        params.put("nt", NetworkUtil.getNetworkTypeName(mAppContext.application()));
        HttpParam.Builder builder = new HttpParam.Builder();
        builder.setUrl(AppConfig.URL.getMemberIntegralUserUrl()).setParams(params);
        return httpHelper.get(builder.build()).getString();
    }

    private IntegralInterface parseJson(String json) throws JSONException {
        IntegralInterface ii = new IntegralInterface();
        if (TextUtils.isEmpty(json)) {
            return ii;
        }

        try {
            JSONObject jos = new JSONObject(json);
            ii.setIntegralValue(jos.getInt("point"));

            ii.setRankValue(jos.getInt("rank"));

            ii.setGrowthValue(jos.getInt("gv"));
        } catch (JSONException e) {
            LogUtil.e(TAG, "parse integral json error: " + e.getMessage());
        } catch (Exception e) {
            LogUtil.e(TAG, "parse integral json error: " + e.getMessage());
        }
        return ii;
    }

    static class Cache {
        ICacheHelper cacheHelper;

        Cache(ICacheHelper cacheHelper) {
            this.cacheHelper = cacheHelper;
        }

        public void cacheIntegral(String json) {
            cacheHelper.put(getCacheKey(), json);
        }

        public String getCacheIntegral() {
            return cacheHelper.getString(getCacheKey());
        }

        private String getCacheKey() {
            return "integral_user_info";
        }
    }

}
