package gionee.gnservice.app.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.constants.ServerConstants
import gionee.gnservice.app.model.entity.base.Data
import gionee.gnservice.app.model.entity.VersionInfo
import gionee.gnservice.app.model.server.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author jv.lee
 * @date 2019/9/3.
 * @description 版本更新信息
 */
class VersionInfoRepository {

    fun versionInfo(): LiveData<VersionInfo> {
        val data = MutableLiveData<VersionInfo>()

        RetrofitUtils.instance.getApi()
            .versionInfo(ServerConstants.ACT_VERSION_INFO)
            .enqueue(object : Callback<Data<VersionInfo>> {
                override fun onFailure(call: Call<Data<VersionInfo>>, t: Throwable) {
                    LogUtil.e("versionInfo failure :" + t.message)
                    data.value = null
                }

                override fun onResponse(call: Call<Data<VersionInfo>>, response: Response<Data<VersionInfo>>) {
                    if (response.body()?.code == 1) {
                        data.value = response.body()?.data
                    }
                }
            })

        return data
    }

}