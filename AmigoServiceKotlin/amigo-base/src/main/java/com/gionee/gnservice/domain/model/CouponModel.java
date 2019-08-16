package com.gionee.gnservice.domain.model;

import android.text.TextUtils;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.http.HttpParam;
import com.gionee.gnservice.common.http.IHttpHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.config.EnvConfig;
import com.gionee.gnservice.domain.Observable;
import com.gionee.gnservice.entity.CouponInfo;
import com.gionee.gnservice.exception.NetWorkException;
import com.gionee.gnservice.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CouponModel extends BaseModel {
    private static final String TAG = CouponModel.class.getSimpleName();
    private static final String PRIVATE_KEY = "giOnee#$!56f1100b2cfcc7c0bbd47dcb&";
    private static final String PRIVATE_KEY_TEST = "56f0b4ed2cfc42f11204962c";

    public CouponModel(IAppContext appContext) {
        super(appContext);
    }

    public Observable<List<CouponInfo>> getCouponsUseable(final Integer mPageNumber, final Integer mPageSize) {
        LogUtil.d(TAG, "getCouponsUseable");
        return getCoupons(3, mPageNumber, mPageSize);
    }

    public Observable<List<CouponInfo>> getCouponsExpired(final Integer mPageNumber, final Integer mPageSize) {
        LogUtil.d(TAG, "getCouponsExpired");
        return getCoupons(0, mPageNumber, mPageSize);
    }

    public Observable<Integer> getCouponsUseableSize(final String userId) {
        LogUtil.d(TAG, "getCouponsUseableSize");
        return new Observable<Integer>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    if (!isNetworkConnect()) {
                        publishNext(getCacheUseableSize());
                        return null;
                    }

                    if (!isNetworkConnect()) {
                        publishError(new NetWorkException());
                        return null;
                    }
                    IHttpHelper httpHelper = mAppContext.httpHelper();
                    Map<String, String> parmas = new LinkedHashMap<String, String>();

                    String apiKey = AppConfig.Account.getAmigoServiceAppId();
                    String uuid = userId;
                    String type = String.valueOf(1);
                    String pageNumber = String.valueOf(1);
                    String pageSize = String.valueOf(Integer.MAX_VALUE);
                    parmas.put("api_key", AppConfig.Account.getAmigoServiceAppId());
                    parmas.put("uuid", uuid);
                    parmas.put("type", type);
                    parmas.put("page_no", pageNumber);
                    parmas.put("page_size", pageSize);
                    parmas.put("token", getToken(apiKey, uuid, type, pageNumber, pageSize));
                    HttpParam.Builder builder = new HttpParam.Builder();
                    builder.setUrl(AppConfig.URL.getCouponUrl())
                            .setParams(parmas);
                    String response = httpHelper.post(builder.build()).getString();

                    if (TextUtils.isEmpty(response)) {
                        publishError(new Exception("get response is null"));
                        return null;
                    }
                    List<CouponInfo> couponInfos = parseJson(response);
                    int size = couponInfos == null ? 0 : couponInfos.size();
                    publishNext(size);
                    cacheUseableSize(size);
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    publishError(e);
                }
                return null;
            }
        };
    }

    private Observable<List<CouponInfo>> getCoupons(final int mType, final int mPageNumber, final int mPageSize) {
        return new Observable<List<CouponInfo>>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    if (!isNetworkConnect()) {
                        publishError(new NetWorkException());
                        return null;
                    }
                    if (mPageNumber < 0 || mPageSize < 0) {
                        publishError(new IllegalArgumentException("number or size is not vaild"));
                        return null;
                    }
                    IHttpHelper httpHelper = mAppContext.httpHelper();
                    Map<String, String> parmas = new LinkedHashMap<String, String>();

                    String apiKey = AppConfig.Account.getAmigoServiceAppId();
                    String uuid = mAppContext.accountHelper().getUserId();
                    String type = String.valueOf(mType);
                    String pageNumber = String.valueOf(mPageNumber);
                    String pageSize = String.valueOf(mPageSize);
                    parmas.put("api_key", AppConfig.Account.getAmigoServiceAppId());
                    parmas.put("uuid", uuid);
                    parmas.put("type", type);
                    parmas.put("page_no", pageNumber);
                    parmas.put("page_size", pageSize);
                    parmas.put("token", getToken(apiKey, uuid, type, pageNumber, pageSize));
                    HttpParam.Builder builder = new HttpParam.Builder();
                    builder.setUrl(AppConfig.URL.getCouponUrl())
                            .setParams(parmas);
                    String response = httpHelper.post(builder.build()).getString();

                    if (TextUtils.isEmpty(response)) {
                        publishError(new Exception("get response is null"));
                        return null;
                    }
                    publishNext(parseJson(response));

                } catch (Exception e) {
                    e.printStackTrace();
                    publishError(e);
                }
                return null;
            }
        };
    }

    private List<CouponInfo> parseJson(String json) throws JSONException {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        List<CouponInfo> couponInfos = new ArrayList<CouponInfo>();
        JSONObject jos = new JSONObject(json);
        int status = jos.getInt("status");
        String desc = jos.getString("desc");
        LogUtil.d(TAG, "status=" + status + ";desc=" + desc);
        JSONArray dataArray = jos.getJSONArray("data");
        for (int i = 0; i < dataArray.length(); i++) {
            CouponInfo couponInfo = new CouponInfo();
            JSONObject jo = dataArray.getJSONObject(i);
            couponInfo.setAno(jo.getString("ano"));
            couponInfo.setDenomination((int) jo.getDouble("denomination"));
            couponInfo.setBalance((float) jo.getDouble("balance"));
            couponInfo.setStartTime(jo.getString("start_time"));
            couponInfo.setEndTime(jo.getString("end_time"));
            couponInfo.setAppName(jo.getString("app_name"));
            couponInfo.setTaskId(jo.getString("task_id"));
            couponInfo.setUseAmount((float) jo.getDouble("use_amount"));
            couponInfo.setStatus(jo.getInt("status"));
            couponInfo.setType(jo.getInt("coupon_type"));
            couponInfo.setUserAppName(jo.getString("use_app_name"));
            couponInfos.add(couponInfo);
        }

        return couponInfos;
    }

    private int getCacheUseableSize() {
        String value = mAppContext.cacheHelper().getString("useable_size");
        return (!TextUtils.isEmpty(value)) ? Integer.parseInt(value) : 0;
    }

    private void cacheUseableSize(int size) {
        mAppContext.cacheHelper().put("useable_size", String.valueOf(size));
    }

    private String getToken(String apiKey, String uuid, String type, String pageNo, String pageSize) {
        try {
            String privateKey = EnvConfig.isProEnv() ? PRIVATE_KEY : PRIVATE_KEY_TEST;
            return getMD5(privateKey + apiKey + pageNo + pageSize + type + uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getMD5(String plaintext) throws Exception {
        return getMD5(plaintext, "UTF-8");
    }

    private String getMD5(String plaintext, String charsetName) throws Exception {
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
}
