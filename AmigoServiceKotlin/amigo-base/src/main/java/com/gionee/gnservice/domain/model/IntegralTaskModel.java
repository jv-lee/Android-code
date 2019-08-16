package com.gionee.gnservice.domain.model;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.http.HttpParam;
import com.gionee.gnservice.common.http.IHttpHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.domain.Observable;
import com.gionee.gnservice.entity.IntegralTask;
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
 * Created by caocong on 5/4/17.
 */

public class IntegralTaskModel extends BaseModel {
    private static final String TAG = IntegralTaskModel.class.getSimpleName();

    public IntegralTaskModel(IAppContext appContext) {
        super(appContext);
    }

    public Observable<List<IntegralTask>> getIntegralTasks(final String token) {
        LogUtil.d(TAG, "getIntegralTask" + this.toString());
        return new Observable<List<IntegralTask>>() {
            @Override
            protected Object doInBackground(Object... p) {
                try {
                    if (!isNetworkConnect()) {
                        publishError(new NetWorkException());
                        return null;
                    }

                    IHttpHelper httpHelper = mAppContext.httpHelper();
                    Map<String, String> params = new LinkedHashMap<String, String>();
                    params.put("model", Build.MODEL);
                    params.put("imei", GNDecodeUtils.get(PhoneUtil.getIMEI(mAppContext.application())));
                    LogUtil.d(TAG, "get token is:" + token);
                    params.put("token", Base64.encodeToString(token.getBytes("utf-8"), Base64.DEFAULT));
                    params.put("ch", SdkUtil.AMIGO_SERVICE_PACKAGE_NAME);
                    params.put("nt", NetworkUtil.getNetworkTypeName(mAppContext.application()));
                    HttpParam.Builder builder = new HttpParam.Builder();
                    builder.setUrl(AppConfig.URL.getMemberIntegralMakeUrl()).setParams(params);
                    String response = httpHelper.get(builder.build()).getString();
                    LogUtil.d(TAG, "get integral task is=" + response);
                    publishNext(parseJson(response));

                } catch (Exception e) {
                    LogUtil.e(TAG, "getIntegralTasks throw exception.");
                    publishError(e);
                }
                return null;
            }
        };
    }

    private List<IntegralTask> parseJson(String json) throws JSONException {
        List<IntegralTask> integralTasks = new ArrayList<IntegralTask>();
        if (TextUtils.isEmpty(json)) {
            return integralTasks;
        }
        JSONArray dataArray = new JSONArray(json);
        for (int i = 0; i < dataArray.length(); i++) {
            IntegralTask task = new IntegralTask();
            JSONObject jo = dataArray.getJSONObject(i);
            task.setId(jo.getInt("i"));
            task.setType(jo.getInt("t"));
            task.setContent(jo.getString("cn"));
            task.setName(jo.getString("name"));
            task.setDescription(jo.getString("desc"));
            task.setValue(jo.getInt("p"));
            task.setIntegralType(jo.getInt("pt"));
            task.setIconUrl(jo.getString("icon"));
            task.setStatus(jo.getInt("fst"));
            integralTasks.add(task);
        }
        return integralTasks;
    }

}
