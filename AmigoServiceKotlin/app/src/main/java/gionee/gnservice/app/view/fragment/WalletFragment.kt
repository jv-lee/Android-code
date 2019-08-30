package gionee.gnservice.app.view.fragment


import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.webkit.JavascriptInterface
import com.lee.library.base.BaseFragment
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.BuildConfig
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.databinding.FragmentWalletBinding
import gionee.gnservice.app.view.activity.GameActivity
import gionee.gnservice.app.view.activity.WebActivity
import gionee.gnservice.app.view.native.ADInterface
import gionee.gnservice.app.view.native.JSInterface

/**
 * A simple [Fragment] subclass.
 *
 */
class WalletFragment : BaseFragment<FragmentWalletBinding, ViewModel>(R.layout.fragment_wallet, null) {

    override fun bindData(savedInstanceState: Bundle?) {
        binding.web.addJavascriptInterface(JSInterface(context!!.applicationContext), "client")
        binding.web.addJavascriptInterface(this, "wallet")
        binding.web.addJavascriptInterface(ADInterface(activity!!), "ad")
        binding.web.loadUrl(BuildConfig.WALLET_URI)
    }

    override fun bindView() {
    }

    //通用web页面跳转
    @JavascriptInterface
    fun startActivity(url: String, title: String?) {
        activity?.runOnUiThread {
            startActivity(
                Intent(activity, WebActivity::class.java)
                    .putExtra(Constants.URL, url)
                    .putExtra(Constants.V_TYPE, 1)
                    .putExtra(Constants.TITLE, title)
            )
        }
    }

    //本地钱包子页面跳转路径  BuildConfig.JS_URI + 页面地址
    @JavascriptInterface
    fun startLocalActivity(url: String, title: String?) {
        activity?.runOnUiThread {
            startActivity(
                Intent(activity, WebActivity::class.java)
                    .putExtra(Constants.URL, BuildConfig.JS_URI + url)
                    .putExtra(Constants.V_TYPE, 1)
                    .putExtra(Constants.TITLE, title)
            )
        }
    }

    @JavascriptInterface
    fun checkRedPoint() {

    }

    @JavascriptInterface
    fun isLogin(): Boolean {
        return true
    }

    @JavascriptInterface
    fun goLogin() {

    }

    @JavascriptInterface
    fun isConnected(): Boolean {

        return true
    }

    @JavascriptInterface
    fun startGame(url: String, vtype: Int, gid: String) {
        activity?.runOnUiThread {
            startActivity(
                Intent(activity, GameActivity::class.java)
                    .putExtra(Constants.URL, url)
                    .putExtra(Constants.V_TYPE, vtype)
            )
        }
    }

    @JavascriptInterface
    fun wxLogin() {
    }


    @JavascriptInterface
    fun startPage(code: Int, arg: String) {
    }


    override fun onFragmentResume() {
        super.onFragmentResume()
        binding.web.loadUrl("javascript:facthHomeData()")
    }

}
