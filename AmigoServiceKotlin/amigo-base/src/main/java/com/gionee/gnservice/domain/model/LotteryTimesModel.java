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
public class LotteryTimesModel extends BaseModel {
    private static final String TAG = LotteryTimesModel.class.getSimpleName();
    private Cache mCache;

    public LotteryTimesModel(IAppContext appContext) {
        super(appContext);
        mCache = new Cache(appContext.cacheHelper());
    }

    public Observable<Integer> getLottyRemainTimes(final String token) {
        return new Observable<Integer>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    if (!isNetworkConnect()) {
                        publishNext(Integer.parseInt(mCache.getCacheTimes()));
                        return null;
                    }
                    String response = loadDataFromNet(token);
                    LogUtil.d(TAG, "get lottery remain time is:" + response);
                    if (!TextUtils.isEmpty(response)) {
                        mCache.cacheTimes(response);
                        publishNext(parseJson(response));
                        return null;
                    }
                    publishNext(0);

                } catch (Exception e) {
                    e.printStackTrace();
                    publishError(e);
                }
                return null;
            }
        };
    }

    private String loadDataFromNet(String token) throws Exception {
        LogUtil.d(TAG, "load lottery remain times from net");
        IHttpHelper httpHelper = mAppContext.httpHelper();
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("model", Build.MODEL);
        params.put("imei", GNDecodeUtils.get(PhoneUtil.getIMEI(mAppContext.application())));
        params.put("nt", NetworkUtil.getNetworkTypeName(mAppContext.application()));
        params.put("ch", SdkUtil.AMIGO_SERVICE_PACKAGE_NAME);
        params.put("token", Base64.encodeToString(token.getBytes("utf-8"), Base64.DEFAULT));
        HttpParam.Builder builder = new HttpParam.Builder();
        builder.setUrl(AppConfig.URL.getMemberIntegralLotteryTimesUrl()).setParams(params);
        return httpHelper.get(builder.build()).getString();
    }

    private int parseJson(String json) throws JSONException {
        if (TextUtils.isEmpty(json)) {
            return 0;
        }
        JSONObject jos = new JSONObject(json);
        return jos.getInt("rc");
    }

    static class Cache {
        ICacheHelper cacheHelper;

        Cache(ICacheHelper cacheHelper) {
            this.cacheHelper = cacheHelper;
        }

        public void cacheTimes(String json) {
            cacheHelper.put(getCacheKey(), json);
        }

        public String getCacheTimes() {
            return cacheHelper.getString(getCacheKey());
        }

        private String getCacheKey() {
            return "lottey_remain_times";
        }
    }

}
