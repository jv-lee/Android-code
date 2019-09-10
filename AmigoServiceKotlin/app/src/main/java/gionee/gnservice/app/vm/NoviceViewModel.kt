package gionee.gnservice.app.vm

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.lee.library.mvvm.BaseViewModel
import gionee.gnservice.app.model.entity.VideoData
import gionee.gnservice.app.model.repository.NoviceRepository

/**
 * @author jv.lee
 * @date 2019/9/10.
 * @description
 */
class NoviceViewModel(application: Application) : BaseViewModel(application) {

    val model by lazy { NoviceRepository() }
    val videoData by lazy { MutableLiveData<VideoData>() }

    fun getVideoData() {
        model.getVideoData().observeForever {
            videoData.value = it
        }
    }

}