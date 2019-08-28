package gionee.gnservice.app.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.lee.library.mvvm.IModel
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.constants.ServerConstants
import gionee.gnservice.app.model.entity.Data
import gionee.gnservice.app.model.entity.Video
import gionee.gnservice.app.model.entity.VideoCategory
import gionee.gnservice.app.model.server.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author jv.lee
 * @date 2019/8/28.
 * @description
 */
class VideoRepository : IModel {

    fun videoCategory(): LiveData<VideoCategory> {
        val data = MutableLiveData<VideoCategory>()

        RetrofitUtils.instance
            .getApi()
            .videoCategory(ServerConstants.ACT_VIDEO_CATEGORY)
            .enqueue(object : Callback<Data<VideoCategory>> {
                override fun onFailure(call: Call<Data<VideoCategory>>, t: Throwable?) {
                    LogUtil.e(t?.message)
                }

                override fun onResponse(call: Call<Data<VideoCategory>>, response: Response<Data<VideoCategory>>) {
                    if (response.body()?.code == 1) {
                        data.value = response.body()?.data
                    }
                }

            })
        return data
    }

    fun videoList(cid: String, page: Int, limit: Int): LiveData<Video> {
        val data = MutableLiveData<Video>()
        val map = HashMap<String, Any>()
        map.put("cid", cid)
        map.put("page", page)
        map.put("limit", limit)

        RetrofitUtils.instance
            .getApi()
            .videoList(ServerConstants.ACT_VIDEO_LIST, map)
            .enqueue(object : Callback<Data<Video>> {
                override fun onFailure(call: Call<Data<Video>>, t: Throwable) {
                    LogUtil.e(t.message)
                }

                override fun onResponse(call: Call<Data<Video>>, response: Response<Data<Video>>) {
                    if (response.body()?.code == 1) {
                        data.value = response.body()?.data
                    }
                }
            })
        return data
    }

}