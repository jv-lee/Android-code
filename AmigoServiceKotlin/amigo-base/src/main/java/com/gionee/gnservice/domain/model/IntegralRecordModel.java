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
import com.gionee.gnservice.entity.IntegralRecord;
import com.gionee.gnservice.exception.NetWorkException;
import com.gionee.gnservice.utils.GNDecodeUtils;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.NetworkUtil;
import com.gionee.gnservice.utils.PhoneUtil;
import com.gionee.gnservice.utils.SdkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caocong on 6/1/17.
 */
public class IntegralRecordModel extends BaseModel {
    private static final String TAG = IntegralRecordModel.class.getSimpleName();
    private Cache mCache;

    public IntegralRecordModel(IAppContext appContext) {
        super(appContext);
        mCache = new Cache(appContext.cacheHelper());
    }

    public Observable<List<IntegralRecord>> getIntegralRecords(final int page, final String token, final int type) {
        LogUtil.d(TAG, "getIntegralRecords page is:" + page + ";type is:" + type);
        return new Observable<List<IntegralRecord>>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    String response = loadDataFromNet(token, page, type);
                    LogUtil.d(TAG, "get integral record is:" + response);
                    List<IntegralRecord> records = parseJson(response);
                    publishNext(records);
                } catch (Exception e) {
                    e.printStackTrace();
                    publishError(new NetWorkException());
                }
                return null;
            }
        };
    }

    private String loadDataFromNet(String token, int page, int type) throws Exception {
        LogUtil.d(TAG, "load integral record from net");
        IHttpHelper httpHelper = mAppContext.httpHelper();
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("model", Build.MODEL);
        params.put("imei", GNDecodeUtils.get(PhoneUtil.getIMEI(mAppContext.application())));
        params.put("ch", SdkUtil.AMIGO_SERVICE_PACKAGE_NAME);
        params.put("token", Base64.encodeToString(token.getBytes("utf-8"), Base64.DEFAULT));
        params.put("pg", String.valueOf(page));
        params.put("nt", NetworkUtil.getNetworkTypeName(mAppContext.application()));
        params.put("t", String.valueOf(type));
        HttpParam.Builder builder = new HttpParam.Builder();
        builder.setUrl(AppConfig.URL.getMemberIntegralRecordUrl()).setParams(params);
        return httpHelper.get(builder.build()).getString();
    }

    private List<IntegralRecord> parseJson(String json) throws JSONException {
        List<IntegralRecord> integralRecords = new ArrayList<IntegralRecord>();
        if (TextUtils.isEmpty(json)) {
            return integralRecords;
        }
        JSONObject jos = new JSONObject(json);
        JSONArray catesArray = jos.getJSONArray("list");
        for (int i = 0; i < catesArray.length(); i++) {
            IntegralRecord record = new IntegralRecord();
            JSONObject jo = catesArray.getJSONObject(i);
            record.setAction(jo.getString("action"));
            record.setCreateTime(jo.getString("createtime"));
            record.setScore(jo.getInt("score"));
            record.setAppName(jo.getString("appname"));
            integralRecords.add(record);
        }
        return integralRecords;
    }

    static class Cache {
        ICacheHelper cacheHelper;

        Cache(ICacheHelper cacheHelper) {
            this.cacheHelper = cacheHelper;
        }

        public void cacheRecords(String json) {
            cacheHelper.put(getCacheKey(), json);
        }

        public String getCacheRecords() {
            return cacheHelper.getString(getCacheKey());
        }

        private String getCacheKey() {
            return "integral_records";
        }
    }

}
