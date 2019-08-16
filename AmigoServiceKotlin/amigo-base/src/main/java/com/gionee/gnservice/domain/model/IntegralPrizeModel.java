package com.gionee.gnservice.domain.model;

import android.os.Build;
import android.text.TextUtils;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.cache.ICacheHelper;
import com.gionee.gnservice.common.http.HttpParam;
import com.gionee.gnservice.common.http.IHttpHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.domain.Observable;
import com.gionee.gnservice.utils.GNDecodeUtils;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.NetworkUtil;
import com.gionee.gnservice.utils.PhoneUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caocong on 5/31/17.
 */
public class IntegralPrizeModel extends BaseModel {
    private static final String TAG = IntegralPrizeModel.class.getSimpleName();
    private Cache mCache;

    public IntegralPrizeModel(IAppContext appContext) {
        super(appContext);
        mCache = new Cache(appContext.cacheHelper());
    }

    public Observable<List<String>> getIntegralAllPrizes() {
        return getIntegralPrizes(0);
    }

    public Observable<List<String>> getIntegralMakePrizes() {
        return getIntegralPrizes(1);
    }

    private Observable<List<String>> getIntegralPrizes(final int type) {
        return new Observable<List<String>>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    if (!isNetworkConnect()) {
                        //publishNext(parseJson(mCache.getCachePrizes()));
                        publishNext(parseJson(null));
                        return null;
                    }
                    String response = loadDataFromNet(type);
                    mCache.cachePrizes(response);
                    publishNext(parseJson(response));
                    LogUtil.d(TAG, "get prize info is:" + response);
                } catch (Exception e) {
                    e.printStackTrace();
                    publishError(e);
                }
                return null;
            }
        };
    }

    private String loadDataFromNet(int type) throws Exception {
        LogUtil.d(TAG, "load member prize from net,type is:" + type);
        IHttpHelper httpHelper = mAppContext.httpHelper();
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("model", Build.MODEL);
        params.put("imei", GNDecodeUtils.get(PhoneUtil.getIMEI(mAppContext.application())));
        params.put("nt", NetworkUtil.getNetworkTypeName(mAppContext.application()));
        params.put("type", String.valueOf(type));
        HttpParam.Builder builder = new HttpParam.Builder();
        builder.setUrl(AppConfig.URL.getMemberIntegralPrizeUrl()).setParams(params);
        return httpHelper.get(builder.build()).getString();
    }

    private List<String> parseJson(String json) throws JSONException {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        List<String> prizes = new ArrayList<String>();
        JSONObject jos = new JSONObject(json);
        JSONArray catesArray = jos.getJSONArray("cn");
        for (int i = 0; i < catesArray.length(); i++) {
            prizes.add(catesArray.getString(i));
        }
        return prizes;
    }

    static class Cache {
        ICacheHelper cacheHelper;

        Cache(ICacheHelper cacheHelper) {
            this.cacheHelper = cacheHelper;
        }

        public void cachePrizes(String json) {
            cacheHelper.put(getCacheKey(), json);
        }

        public String getCachePrizes() {
            return cacheHelper.getString(getCacheKey());
        }

        private String getCacheKey() {
            return "integral_prize";
        }
    }

}
