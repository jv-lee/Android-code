package gionee.gnservice.app.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.lee.library.mvvm.IModel
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.constants.ServerConstants
import gionee.gnservice.app.model.entity.VideoData
import gionee.gnservice.app.model.entity.base.Data
import gionee.gnservice.app.model.server.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author jv.lee
 * @date 2019/9/10.
 * @description
 */
class NoviceRepository : IModel {

    fun getVideoData(): LiveData<VideoData> {
        val data = MutableLiveData<VideoData>()

        RetrofitUtils.instance
            .getApi()
            .getVideoOne(ServerConstants.ACT_VIDEO_ONE)
            .enqueue(object : Callback<Data<VideoData>> {
                override fun onResponse(call: Call<Data<VideoData>>, response: Response<Data<VideoData>>) {
                    if (response.body()?.code == 1) {
                        data.value = response.body()?.data
                    } else {
                        data.value = null
                    }
                }

                override fun onFailure(call: Call<Data<VideoData>>, t: Throwable) {
                    LogUtil.e("getVideoOne failure:${t.message}")
                    data.value = null
                }

            })

        return data
    }

}