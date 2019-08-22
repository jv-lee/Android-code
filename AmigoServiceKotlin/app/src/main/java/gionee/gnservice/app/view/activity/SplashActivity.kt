package gionee.gnservice.app.view.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.lee.library.base.BaseActivity
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.R
import gionee.gnservice.app.databinding.ActivitySplashBinding
import gionee.gnservice.app.model.server.RetrofitUtils
import gionee.gnservice.app.vm.SplashViewModel

class SplashActivity :
    BaseActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash, SplashViewModel::class.java) {

    override fun bindData(savedInstanceState: Bundle?) {
        viewModel.model.login().observe(this, Observer {
            RetrofitUtils.instance.saveSessionKey(it?.sessionKey)
            RetrofitUtils.instance.saveUser(it)
        })
    }

    override fun bindView() {
    }

}
