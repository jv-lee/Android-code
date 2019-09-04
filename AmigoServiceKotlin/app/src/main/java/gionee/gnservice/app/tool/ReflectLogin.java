package gionee.gnservice.app.tool;

import com.lee.library.utils.LogUtil;
import gionee.gnservice.app.App;
import gionee.gnservice.app.constants.ServerConstants;
import gionee.gnservice.app.model.entity.Login;
import gionee.gnservice.app.model.entity.base.Data;
import gionee.gnservice.app.model.server.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;

/**
 * @author jv.lee
 * @date 2019/9/3.
 * @description 提供给外部模块反射调用重新登陆金立用户
 */
public class ReflectLogin {

    public static void login(String uid, String name) {
        HashMap<String, Object> map = new HashMap<>();
        //设置参数
        map.put("imei", CommonTool.Companion.getIMEI(App.instance));
        map.put("eqid", CommonTool.Companion.getEquipID(App.instance));
        map.put("ch", "uc_jl");
        map.put("thirdid", 1);
        map.put("userid", uid);
        map.put("nick", name);
        RetrofitUtils.Companion.getInstance()
                .getApi()
                .login(ServerConstants.ACT_LOGIN, map)
                .enqueue(new Callback<Data<Login>>() {
                    @Override
                    public void onResponse(Call<Data<Login>> call, Response<Data<Login>> response) {
                        if (response.code() == 200 && response.body() != null && response.body().getCode() == 1) {
                            RetrofitUtils.Companion.getInstance().saveSessionKey(response.body().getData().getSessionKey());
                            RetrofitUtils.Companion.getInstance().saveUser(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<Data<Login>> call, Throwable t) {
                        LogUtil.e("金立登陆失败 :" + t.getMessage());
                    }
                });
    }
}
