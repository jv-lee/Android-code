package com.gionee.gnservice.domain.data;

import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.cache.ICacheHelper;
import com.gionee.gnservice.common.http.HttpParam;
import com.gionee.gnservice.common.http.IHttpHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.domain.Observable;
import com.gionee.gnservice.domain.model.BaseModel;
import com.gionee.gnservice.entity.PrivilegeAction;
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
 * Created by caocong on 2/27/17.
 */
public class PrivilegeActionModel extends BaseModel {
    private static final String TAG = PrivilegeActionModel.class.getSimpleName();
    private Cache mCache;

    public PrivilegeActionModel(@NonNull IAppContext appContext) {
        super(appContext);
        mCache = new Cache(appContext.cacheHelper());
    }

    public Observable<List<PrivilegeAction>> loadPrivilegeActions() {
        return new Observable<List<PrivilegeAction>>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    if (!isNetworkConnect() && mCache.getCachePrivilegeActions() != null) {
                        publishNext((mCache.getCachePrivilegeActions()));
                        return null;
                    }
                    String response = loadDataFromNet();
                    if (!TextUtils.isEmpty(response)) {
                        mCache.cachePrivilegeActions(response);
                        publishNext(parseJson(response));
                        return null;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    publishError(e);
                }
                return null;
            }
        };
    }

    private String loadDataFromNet() throws Exception {
        LogUtil.d(TAG, "load lottery remain times from net");
        IHttpHelper httpHelper = mAppContext.httpHelper();
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("model", Build.MODEL);
        params.put("imei", GNDecodeUtils.get(PhoneUtil.getIMEI(mAppContext.application())));
        params.put("nt", NetworkUtil.getNetworkTypeName(mAppContext.application()));
        params.put("module", String.valueOf(2));

        HttpParam.Builder builder = new HttpParam.Builder();
        builder.setUrl(AppConfig.URL.getPrivilegeActionUrl()).setParams(params);
        return httpHelper.get(builder.build()).getString();
    }

    private List<PrivilegeAction> parseJson(String json) throws JSONException {
        List<PrivilegeAction> privilegeActions = new ArrayList<PrivilegeAction>();
        if (TextUtils.isEmpty(json)) {
            return privilegeActions;
        }
        JSONArray dataArray = new JSONArray(json);
        for (int i = 0; i < dataArray.length(); i++) {
            PrivilegeAction action = new PrivilegeAction();
            JSONObject jo = dataArray.getJSONObject(i);
            action.setId(jo.getString("i"));
            action.setName(jo.getString("n"));
            action.setType(jo.getInt("t"));
            action.setImageUrl(jo.getString("img"));
            action.setContent(jo.getString("cn"));
            action.setDescription(jo.getString("desc"));
            privilegeActions.add(action);
        }
        return privilegeActions;
    }

    class Cache {
        private static final long EXPIRED_TIME = 60 * 60 * 12;
        ICacheHelper cacheHelper;

        Cache(ICacheHelper cacheHelper) {
            this.cacheHelper = cacheHelper;
        }

        public void cachePrivilegeActions(String json) {
            cacheHelper.put(getCacheKey(), json);
        }

        public List<PrivilegeAction> getCachePrivilegeActions() {

            ICacheHelper.IExpiredStrategy strategy = new ICacheHelper.IExpiredStrategy() {
                @Override
                public boolean isExpired(long lastSaveTimeMills) {
                    return System.currentTimeMillis() - lastSaveTimeMills >= EXPIRED_TIME;
                }
            };
            if (cacheHelper.isExpired(getCacheKey(), strategy)) {
                LogUtil.d(TAG, "cache is expired");
                cacheHelper.remove(getCacheKey());
                return null;
            } else {
                LogUtil.d(TAG, "use cache");
                String json = cacheHelper.getString(getCacheKey());
                if (TextUtils.isEmpty(json)) {
                    LogUtil.d(TAG, "get cache is null");
                    return null;
                }
                try {
                    return parseJson(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        private String getCacheKey() {
            return "integral_user";
        }
    }

}
