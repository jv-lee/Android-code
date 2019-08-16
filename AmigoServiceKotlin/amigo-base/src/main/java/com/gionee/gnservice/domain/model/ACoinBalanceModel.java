package com.gionee.gnservice.domain.model;

import android.text.TextUtils;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.cache.ICacheHelper;
import com.gionee.gnservice.common.http.HttpParam;
import com.gionee.gnservice.common.http.IHttpHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.config.EnvConfig;
import com.gionee.gnservice.domain.Observable;
import com.gionee.gnservice.utils.LogUtil;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by caocong on 2/27/17.
 */

public class ACoinBalanceModel extends BaseModel {
    private static final String TAG = ACoinBalanceModel.class.getSimpleName();
    private static final String PRIVATE_KEY = "giOnee#$!56f1100b2cfcc7c0bbd47dcb&";
    private static final String PRIVATE_KEY_TEST = "56f0b4ed2cfc42f11204962c";
    private Cache mCache;

    public ACoinBalanceModel(IAppContext appContext) {
        super(appContext);
        mCache = new Cache(appContext.cacheHelper());
    }

    public Observable<String> getACoinBalance(final String userId) {
        LogUtil.d(TAG, "getDataFromRemote");
        return new Observable<String>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    if (!isNetworkConnect()) {
                        publishNext(mCache.getCacheAcoin());
                        return null;
                    }
                    IHttpHelper httpHelper = mAppContext.httpHelper();
                    Map<String, String> parmas = new LinkedHashMap<String, String>();
                    parmas.put("token", getToken(AppConfig.Account.getAmigoServiceAppId(), userId));
                    parmas.put("api_key", AppConfig.Account.getAmigoServiceAppId());
                    parmas.put("uuid", userId);

                    HttpParam.Builder builder = new HttpParam.Builder();
                    builder.setUrl(AppConfig.URL.getACoinBalanceUrl()).setParams(parmas);
                    String response = httpHelper.post(builder.build()).getString();
                    LogUtil.i(TAG, "ACoin = " + response);
                    if (TextUtils.isEmpty(response)) {
                        publishError(new Exception("get response is null"));
                        return null;
                    }
                    JSONObject jsonObject = new JSONObject(response);
                    String balance = jsonObject.getString("gold_coin");
                    String formatCoin = String.format("%.2f", new Float(balance));
                    mCache.cacheAcoin(formatCoin);
                    publishNext(formatCoin);
                } catch (Exception e) {
                    e.printStackTrace();
                    publishError(e);
                }
                return null;
            }
        };
    }

    private String getToken(String appId, String userId) {
        try {
            String privateKey = EnvConfig.isProEnv() ? PRIVATE_KEY : PRIVATE_KEY_TEST;
            return getMD5(privateKey + appId + userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMD5(String plaintext) throws Exception {
        return getMD5(plaintext, "UTF-8");
    }

    public String getMD5(String plaintext, String charsetName) throws Exception {
        if (charsetName == null) {
            throw new NullPointerException();
        }
        String s;
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(plaintext.getBytes(charsetName));
        byte[] tmp = md.digest();
        char[] str = new char[16 * 2];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte byte0 = tmp[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        s = new String(str);
        return s;
    }

    static class Cache {
        ICacheHelper cacheHelper;

        Cache(ICacheHelper cacheHelper) {
            this.cacheHelper = cacheHelper;
        }

        public void cacheAcoin(String acoin) {
            cacheHelper.put("gold_coin", acoin);
        }

        public String getCacheAcoin() {
            return cacheHelper.getString("gold_coin");
        }
    }

}
