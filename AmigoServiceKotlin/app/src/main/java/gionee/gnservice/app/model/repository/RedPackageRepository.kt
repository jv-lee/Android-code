package gionee.gnservice.app.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.lee.library.mvvm.IModel
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.constants.ServerConstants
import gionee.gnservice.app.model.entity.RedPacketAward
import gionee.gnservice.app.model.entity.RedPacketInfo
import gionee.gnservice.app.model.entity.base.Data
import gionee.gnservice.app.model.server.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author jv.lee
 * @date 2019/9/3.
 * @description
 */
class RedPackageRepository : IModel {

    fun getRedPackageRepository(): LiveData<RedPacketAward> {
        val data = MutableLiveData<RedPacketAward>()

        RetrofitUtils.instance
            .getApi()
            .redPackageAward(ServerConstants.ACT_RED_PACKAGE_AWARD)
            .enqueue(object : Callback<Data<RedPacketAward>> {

                override fun onFailure(call: Call<Data<RedPacketAward>>, t: Throwable) {
                    LogUtil.e("redPackageAward failure")
                    data.value = null
                }

                override fun onResponse(call: Call<Data<RedPacketAward>>, response: Response<Data<RedPacketAward>>) {
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