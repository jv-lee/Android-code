package com.gionee.gnservice.domain.model;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.http.HttpParam;
import com.gionee.gnservice.common.http.IHttpHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.domain.Observable;
import com.gionee.gnservice.entity.IntegralTaskResult;
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
 * Created by caocong on 5/4/17.
 */

public class IntegralTaskUploadModel extends BaseModel {
    private static final String TAG = IntegralTaskUploadModel.class.getSimpleName();

    public IntegralTaskUploadModel(IAppContext appContext) {
        super(appContext);
    }

    public Observable<IntegralTaskResult> uploadIntegralTask(final int id, final String token) {
        LogUtil.d(TAG, "uploadIntegralTask" + this.toString());
        return new Observable<IntegralTaskResult>() {
            @Override
            protected Object doInBackground(Object... p) {
                try {
                    IHttpHelper httpHelper = mAppContext.httpHelper();
                    Map<String, String> params = new LinkedHashMap<String, String>();
                    params.put("model", Build.MODEL);
                    params.put("imei", GNDecodeUtils.get(PhoneUtil.getIMEI(mAppContext.application())));
                    params.put("token", Base64.encodeToString(token.getBytes("utf-8"), Base64.DEFAULT));
                    params.put("nt", NetworkUtil.getNetworkTypeName(mAppContext.application()));
                    params.put("ch", SdkUtil.AMIGO_SERVICE_PACKAGE_NAME);
                    params.put("id", String.valueOf(id));
                    HttpParam.Builder builder = new HttpParam.Builder();
                    builder.setUrl(AppConfig.URL.getMemberIntegralTaskUploadUrl()).setParams(params);
                    String response = httpHelper.get(builder.build()).getString();
                    if (!TextUtils.isEmpty(response)) {
                        publishNext(parseJson(response));
                    } else {
                        publishNext(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    publishError(e);
                }
                return null;
            }
        };
    }

    private IntegralTaskResult parseJson(String json) throws JSONException {
        JSONObject jo = new JSONObject(json);
        IntegralTaskResult result = new IntegralTaskResult();
        result.setCode(jo.getInt("r"));
        try {
            result.setId(jo.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
