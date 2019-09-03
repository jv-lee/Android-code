package gionee.gnservice.app.vm

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.lee.library.mvvm.BaseViewModel
import gionee.gnservice.app.model.entity.RedPacketAward
import gionee.gnservice.app.model.entity.RedPacketInfo
import gionee.gnservice.app.model.repository.RedPackageRepository

/**
 * @author jv.lee
 * @date 2019/9/3.
 * @description
 */
class RedPackageViewModel(application: Application) : BaseViewModel(application) {

    val model by lazy { RedPackageRepository() }
    val redPackageAward by lazy { MutableLiveData<RedPacketAward>() }

    fun getRedPackageAward() {
        model.getRedPackageRepository().observeForever {
            redPackageAward.value = it
        }
    }

}