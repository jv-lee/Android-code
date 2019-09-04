package gionee.gnservice.app.vm

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.lee.library.mvvm.BaseViewModel
import gionee.gnservice.app.model.entity.TaskInfo
import gionee.gnservice.app.model.repository.TimeRepository

/**
 * @author jv.lee
 * @date 2019/9/4.
 * @description
 */
class TimeViewModel(application: Application) : BaseViewModel(application) {

    val model by lazy { TimeRepository() }
    val taskInfo by lazy { MutableLiveData<TaskInfo>() }

    fun subAddTime(time: String, type: String, gid: String?) {
        model.subAddTime(time, type, gid).observeForever {
            if (it != null) {
                taskInfo.value = it
            }
        }
    }

}