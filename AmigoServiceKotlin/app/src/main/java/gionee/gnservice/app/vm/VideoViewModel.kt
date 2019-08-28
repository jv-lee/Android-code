package gionee.gnservice.app.vm

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.lee.library.mvvm.BaseViewModel
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.model.entity.Video
import gionee.gnservice.app.model.entity.VideoCategory
import gionee.gnservice.app.model.repository.VideoRepository

/**
 * @author jv.lee
 * @date 2019/8/28.
 * @description
 */
class VideoViewModel(application: Application) : BaseViewModel(application) {

    private val model by lazy { VideoRepository() }
    val videoCategory: MutableLiveData<VideoCategory> = MutableLiveData()
    val videoLists by lazy { MutableLiveData<Video>() }

    fun loadVideoCategory() {
        model.videoCategory().observeForever {
            videoCategory.value = it
        }
    }

    fun loadVideos(cid: String, page: Int, limit: Int) {
        model.videoList(cid, page, limit).observeForever {
            videoLists.value = it
        }
    }

}