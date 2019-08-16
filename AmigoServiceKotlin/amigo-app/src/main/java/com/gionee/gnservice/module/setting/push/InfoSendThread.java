package com.gionee.gnservice.module.setting.push;

import android.content.Context;
import com.gionee.gnservice.common.http.*;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PhoneUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class InfoSendThread extends Thread {
    private WeakReference<Context> mContextRef;
    private String mRid;

    public InfoSendThread(Context context, String rid) {
        mContextRef = new WeakReference<Context>(context);
        mRid = rid;
    }

    @Override
    public void run() {
        Context context = mContextRef.get();
        if (context == null) {
            return;
        }

        sendInfoToAps(context, mRid);

        EmptyService.cancelKeep(context);
    }

//    private void sendInfoToAps(Context context, String rid) {
//        List<NameValuePair> postParam = new ArrayList<NameValuePair>(2);
//        String apsServer = "http://push.gionee.com/aps/AcceptRid";
//
//        postParam.add(new BasicNameValuePair("rid", rid));
//        postParam.add(new BasicNameValuePair("packagename", context.getPackageName()));
//        TelephonyManager telephonyManager = (TelephonyManager) context
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        String imei = telephonyManager.getDeviceId();
//        postParam.add(new BasicNameValuePair("imei", imei));
//
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(apsServer);
//        try {
//            HttpEntity httpEntity = new UrlEncodedFormEntity(postParam, "UTF-8");
//            httpPost.setEntity(httpEntity);
//
//            HttpResponse response = httpClient.execute(httpPost);
//            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                String result = EntityUtils.toString(response.getEntity());
//                System.out.println(result);
//            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
//                String result = EntityUtils.toString(response.getEntity());
//                System.out.println(result);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    private void sendInfoToAps(Context context, String rid) {
        try {
            IHttpHelper httpHelper = new HttpHelperImpls(context);
            HttpParam.Builder builder = new HttpParam.Builder();
            builder.setUrl("http://push.gionee.com/aps/AcceptRid");
            HashMap<String, String> params = new HashMap<>();
            params.put("rid", rid);
            params.put("packagename", context.getPackageName());
            params.put("imei", PhoneUtil.getIMEI(context));
            builder.setParams(params);
            builder.setIsPostParmasJson(false);
            IHttpResponse response = httpHelper.post(builder.build());
            LogUtil.d("Push", "response is" + response.getString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
