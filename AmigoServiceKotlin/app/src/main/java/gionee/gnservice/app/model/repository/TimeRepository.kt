package gionee.gnservice.app.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.lee.library.mvvm.IModel
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.constants.ServerConstants
import gionee.gnservice.app.model.entity.TaskInfo
import gionee.gnservice.app.model.entity.base.Data
import gionee.gnservice.app.model.server.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author jv.lee
 * @date 2019/9/4.
 * @description
 */
class TimeRepository : IModel {

    fun subAddTime(time: String, type: String, gid: String?): LiveData<TaskInfo> {
        val data = MutableLiveData<TaskInfo>()

        RetrofitUtils.instance.getApi()
            .subAddTime(ServerConstants.ACT_ADD_TIME, time, type, gid)
            .enqueue(object : Callback<Data<TaskInfo>> {
                override fun onFailure(call: Call<Data<TaskInfo>>, t: Throwable) {
                    LogUtil.e("subAddTime failure :${t.message}")
                    data.value = null
                }

                override fun onResponse(call: Call<Data<TaskInfo>>, response: Response<Data<TaskInfo>>) {
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