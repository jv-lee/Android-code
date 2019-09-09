package gionee.gnservice.app.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.lee.library.mvvm.IModel
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.constants.ServerConstants
import gionee.gnservice.app.model.entity.WXInfo
import gionee.gnservice.app.model.entity.base.Data
import gionee.gnservice.app.model.server.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author jv.lee
 * @date 2019/9/6.
 * @description
 */
class WeChatRepository : IModel {

    fun bindWX(code: String): LiveData<WXInfo> {
        val data = MutableLiveData<WXInfo>()

        RetrofitUtils.instance.getApi()
            .bindWX(ServerConstants.ACT_BIND_WX, code)
            .enqueue(object : Callback<Data<WXInfo>> {
                override fun onFailure(call: Call<Data<WXInfo>>, t: Throwable) {
                    LogUtil.e("bindWX onFailure :${t.message}")
                    data.value = null
                }

                override fun onResponse(call: Call<Data<WXInfo>>, response: Response<Data<WXInfo>>) {
                    if (response.body()?.code == 1) {
                        data.value = response.body()?.data
                    } else {
                        data.value = null
                    }
                }

            })

        return data
    }

}