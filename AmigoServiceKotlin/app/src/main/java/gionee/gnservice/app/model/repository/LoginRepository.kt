package gionee.gnservice.app.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.gionee.gnservice.common.cache.TCache
import com.gionee.gnservice.entity.AccountInfo
import com.lee.library.mvvm.IModel
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.App
import gionee.gnservice.app.constants.ServerConstants
import gionee.gnservice.app.model.entity.base.Data
import gionee.gnservice.app.model.entity.Login
import gionee.gnservice.app.model.server.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author jv.lee
 * @date 2019/8/22.
 * @description 登陆请求获取数据
 */
class LoginRepository : IModel {

    fun login(): LiveData<Login> {
        val data = MutableLiveData<Login>()
        val map = HashMap<String, Any>()

        val infos = TCache.get(App.instance).getSerializable<AccountInfo>("card/accountinfo")

        //设置参数
        map.put("imei", 1)
        map.put("eqid", 1)
        map.put("ch", 1)

        if (infos?.userId != null) {
            map.put("thirdid", 1)
            map.put("userid", infos.userId)
            map.put("nick", infos.nickName)
        } else {
            map.put("thirdid", 0)
        }

        RetrofitUtils.instance.getApi()
            .login(ServerConstants.ACT_LOGIN, map)
            .enqueue(object : Callback<Data<Login>> {
                override fun onFailure(call: Call<Data<Login>>, t: Throwable?) {
                    LogUtil.e("login ${t?.message}")
                }

                override fun onResponse(call: Call<Data<Login>>, response: Response<Data<Login>>) {
                    if (response.body()?.code == 1) {
                        data.value = response.body()?.data
                    }
                }

            })
        return data
    }
}