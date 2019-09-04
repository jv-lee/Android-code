package gionee.gnservice.app.vm

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.lee.library.mvvm.BaseViewModel
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.Config.Companion.limit
import gionee.gnservice.app.model.entity.TaskInfo
import gionee.gnservice.app.model.entity.Video
import gionee.gnservice.app.model.entity.VideoCategory
import gionee.gnservice.app.model.repository.TimeRepository
import gionee.gnservice.app.model.repository.VideoRepository

/**
 * @author jv.lee
 * @date 2019/8/28.
 * @description
 */
class VideoViewModel(application: Application) : BaseViewModel(application) {

    private val model by lazy { VideoRepository() }
    val videoCategory by lazy { MutableLiveData<VideoCategory>() }
    val videoLists by lazy { MutableLiveData<Video>() }
    private var page: Int = 0

    private val timeModel by lazy { TimeRepository() }
    val taskInfo by lazy { MutableLiveData<TaskInfo>() }


    fun loadVideoCategory() {
        model.videoCategory().observeForever {
            videoCategory.value = it
        }
    }

    fun loadVideos(cid: String) {
        model.videoList(cid, ++page, limit).observeForever {
            videoLists.value = it
        }
    }

    fun refreshVideos(cid: String) {
        page = 1
        model.videoList(cid, page, limit).observeForever {
            videoLists.value = it
        }
    }

    fun subAddTime(time: String, type: String, gid: String?) {
        timeModel.subAddTime(time, type, gid).observeForever {
            if (it != null) {
                taskInfo.value = it
            }
        }
    }


}