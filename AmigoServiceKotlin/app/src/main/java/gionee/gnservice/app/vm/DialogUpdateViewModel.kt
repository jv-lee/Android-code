package gionee.gnservice.app.vm

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.lee.library.mvvm.BaseViewModel
import gionee.gnservice.app.model.entity.VersionInfo
import gionee.gnservice.app.model.repository.VersionInfoRepository

/**
 * @author jv.lee
 * @date 2019/9/3.
 * @description
 */
class DialogUpdateViewModel(application: Application) : BaseViewModel(application) {

    val model by lazy { VersionInfoRepository() }
    val versionInfo by lazy { MutableLiveData<VersionInfo>() }

    fun getVersionInfo() {
        model.versionInfo().observeForever {
            versionInfo.value = it
        }
    }

}