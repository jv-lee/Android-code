package gionee.gnservice.app.vm

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.lee.library.mvvm.BaseViewModel
import gionee.gnservice.app.model.entity.Award
import gionee.gnservice.app.model.repository.VideoDetailsRepository

/**
 * @author jv.lee
 * @date 2019/9/10.
 * @description
 */
class VideoDetailsViewModel(application: Application) : BaseViewModel(application) {

    val model by lazy { VideoDetailsRepository() }
    val award by lazy { MutableLiveData<Award>() }

    fun getVideoAward(type: Int) {
        model.getVideoAward(type).observeForever {
            award.value = it
        }
    }

}