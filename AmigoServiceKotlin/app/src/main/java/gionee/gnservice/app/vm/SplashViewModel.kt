package gionee.gnservice.app.vm

import android.app.Application
import com.lee.library.mvvm.BaseViewModel
import gionee.gnservice.app.model.repository.LoginRepository

/**
 * @author jv.lee
 * @date 2019/8/22.
 * @description
 */
class SplashViewModel(application: Application) : BaseViewModel(application = application) {

    val model by lazy { LoginRepository() }

}