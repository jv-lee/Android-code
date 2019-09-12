package gionee.gnservice.app.view.activity

import android.app.ProgressDialog
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.webkit.JavascriptInterface
import com.gionee.gnservice.wxapi.WXLoginListener
import com.gionee.gnservice.wxapi.WeChatUtil
import com.lee.library.base.BaseActivity
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.databinding.ActivityWebBinding
import gionee.gnservice.app.view.native.ADInterface
import gionee.gnservice.app.view.native.CommonInterface
import gionee.gnservice.app.view.native.JSInterface
import kotlinx.android.synthetic.main.layout_status_toolbar.view.*

/**
 * @author jv.lee
 * @date 2019/8/22.
 * @description 通用H5容器视窗
 */
class WebActivity : BaseActivity<ActivityWebBinding, ViewModel>(R.layout.activity_web, null) {

    var title: String? = null
    var vtype: Int? = 2
    var url: String? = null

    var progressDialog: ProgressDialog? = null

    override fun bindData(savedInstanceState: Bundle?) {
        title = intent.getStringExtra(Constants.TITLE)
        vtype = intent.getIntExtra(Constants.V_TYPE, 2)
        url = intent.getStringExtra(Constants.URL)
    }

    override fun bindView() {
        progressDialog = ProgressDialog(this)

        binding.include.visibility = View.GONE
        if (vtype == 1 && !TextUtils.isEmpty(title)) {
            binding.include.visibility = View.VISIBLE
            binding.include.title.text = title
            binding.include.back.setOnClickListener { finish() }
        }
        binding.web.addJavascriptInterface(JSInterface(applicationContext), JSInterface.NAME)
        binding.web.addJavascriptInterface(CommonInterface(this), CommonInterface.NAME)
        binding.web.addJavascriptInterface(ADInterface(this, binding.web), ADInterface.NAME)
        binding.web.addJavascriptInterface(this, "page")
        binding.web.loadUrl(url)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (binding.web.canGoBack()) {
                binding.web.goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        binding.web.exResume()
    }

    override fun onPause() {
        super.onPause()
        binding.web.exPause()
    }

    override fun onDestroy() {
        binding.web.exDestroy()
        super.onDestroy()
    }

    @JavascriptInterface
    fun wxLogin() {
        runOnUiThread {
            progressDialog?.show()
            WeChatUtil.wxLogin(applicationContext, object : WXLoginListener {
                override fun onSuccess(code: String?) {
                    binding.web.loadUrl("javascript:wxback(1)")
                }

                override fun onFail(errMsg: String?) {
                    binding.web.loadUrl("javascript:wxback(0)")
                }

            })
        }
    }

    @JavascriptInterface
    fun onBack() {
        runOnUiThread {
            if (binding.web.canGoBack()) {
                binding.web.goBack()
            } else {
                finish()
                binding.web.loadUrl("javascript:facthHomeData()")
            }
        }
    }

}
