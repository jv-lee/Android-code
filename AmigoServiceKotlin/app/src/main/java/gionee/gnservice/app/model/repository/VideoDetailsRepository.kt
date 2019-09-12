package gionee.gnservice.app.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.lee.library.mvvm.IModel
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.constants.ServerConstants
import gionee.gnservice.app.model.entity.Award
import gionee.gnservice.app.model.entity.base.Data
import gionee.gnservice.app.model.server.RetrofitUtils
import gionee.gnservice.app.tool.disk.DiskLruCacheImpl
import gionee.gnservice.app.view.fragment.VideoChildFragment.Companion.key
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author jv.lee
 * @date 2019/9/10.
 * @description
 */
class VideoDetailsRepository : IModel {

    fun getVideoAward(type: Int): LiveData<Award> {
        val data = MutableLiveData<Award>()

        RetrofitUtils.instance
            .getApi()
            .getVideoAward(ServerConstants.ACT_VIDEO_AWARD, type.toString())
            .enqueue(object : Callback<Data<Award>> {
                override fun onFailure(call: Call<Data<Award>>, t: Throwable) {
                    LogUtil.e("getVideoAward failure :${t.message}")
                    data.value = null
                }

                override fun onResponse(call: Call<Data<Award>>, response: Response<Data<Award>>) {
                    if (response.body()?.code == 1) {
                        data.value = response.body()?.data
                    } else {
                        data.value = null
                    }
                }

            })

        return data
    }

    private val video_key: String = "video_key"

    fun videoAwardEnable(value: String): LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()
        data.value = true

        val diskLruCache = DiskLruCacheImpl()
        val content = diskLruCache.get(video_key)

        //设置数据
        if (content != null) {
            if (content.contains(value)) {
                data.value = false
            } else {
                diskLruCache.put(video_key, "$content,$value")
            }
        } else {
            diskLruCache.put(video_key, value)
        }

        return data
    }

}