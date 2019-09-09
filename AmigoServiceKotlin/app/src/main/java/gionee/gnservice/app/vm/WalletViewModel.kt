package gionee.gnservice.app.vm

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.lee.library.mvvm.BaseViewModel
import gionee.gnservice.app.model.entity.WXInfo
import gionee.gnservice.app.model.repository.WeChatRepository

/**
 * @author jv.lee
 * @date 2019/9/6.
 * @description
 */
class WalletViewModel(application: Application) : BaseViewModel(application) {

    val wxModel by lazy { WeChatRepository() }
    val wxInfo by lazy { MutableLiveData<WXInfo>() }

    fun bindWX(code: String) {
        wxModel.bindWX(code).observeForever {
            wxInfo.value = it
        }
    }

}