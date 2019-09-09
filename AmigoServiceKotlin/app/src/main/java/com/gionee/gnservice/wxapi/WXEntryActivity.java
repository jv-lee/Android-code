package com.gionee.gnservice.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.gionee.gnservice.utils.LogUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import gionee.gnservice.app.App;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static WXLoginListener wxLoginListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            App.wxApi.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录微信
     *
     * @param context
     * @param sApi
     */
    public static void loginWeixin(Context context, IWXAPI sApi, WXLoginListener listener) {
        if (!check(context)) {
            return;
        }
        wxLoginListener = listener;
        //发送授权登录信息，来获取code
        SendAuth.Req req = new SendAuth.Req();
        //应用的作用域，获取个人信息
        req.scope = "snsapi_userinfo";
        /**
         * 用于保持请求和回调的状态，授权请求后原样带回给第三方
         * 为了防止csrf攻击（跨站请求伪造攻击），后期改为随机数加session来检验
         */
        req.state = "app_user_center";
        boolean s = sApi.sendReq(req);
        LogUtil.d("sendReq : " + s);
    }

    /**
     * 微信发送请求到第三方应用时，会回调到该方法
     *
     * @param req
     */
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
    }

    /**
     * 第三方应用发送到微信的请求处理后的响应结果，会回调该方法
     *
     * @param resp
     */
    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof SendAuth.Resp) {
            //登录回调
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    String code = ((SendAuth.Resp) resp).code;
                    if (wxLoginListener != null) {
                        wxLoginListener.onSuccess(code);
                    }
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    if (wxLoginListener != null) {
                        wxLoginListener.onFail("登录取消");
                    }
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    if (wxLoginListener != null) {
                        wxLoginListener.onFail("登录失败");
                    }
                    break;
                case BaseResp.ErrCode.ERR_UNSUPPORT:
                    if (wxLoginListener != null) {
                        wxLoginListener.onFail("登录失败");
                    }
                    break;
                default:
                    if (wxLoginListener != null) {
                        wxLoginListener.onFail("登录失败");
                    }
                    break;
            }
        } else {
            LogUtil.e("未知操作回调");
        }
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        App.wxApi.handleIntent(intent, this);
    }

    private static boolean check(Context context) {
        if (!App.wxApi.isWXAppInstalled()) {
            Toast.makeText(context, "请先安装微信应用", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
