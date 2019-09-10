package gionee.gnservice.app.view.fragment


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import com.gionee.gnservice.wxapi.WXLoginListener
import com.gionee.gnservice.wxapi.WeChatUtil
import com.lee.library.base.BaseFragment
import com.lee.library.livedatabus.InjectBus
import com.lee.library.livedatabus.LiveDataBus
import com.lee.library.utils.NetworkUtil
import gionee.gnservice.app.BuildConfig
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.EventConstants
import gionee.gnservice.app.databinding.FragmentWalletBinding
import gionee.gnservice.app.tool.CommonTool
import gionee.gnservice.app.view.activity.GameActivity
import gionee.gnservice.app.view.activity.MainActivity
import gionee.gnservice.app.view.activity.WebActivity
import gionee.gnservice.app.view.native.ADInterface
import gionee.gnservice.app.view.native.JSInterface
import gionee.gnservice.app.vm.WalletViewModel

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description 主TAB 钱包板块 （H5）
 */
class WalletFragment :
    BaseFragment<FragmentWalletBinding, WalletViewModel>(R.layout.fragment_wallet, WalletViewModel::class.java) {

    var progressDialog: ProgressDialog? = null

    override fun bindData(savedInstanceState: Bundle?) {
        binding.web.addJavascriptInterface(JSInterface(context!!.applicationContext), JSInterface.NAME)
        binding.web.addJavascriptInterface(this, "wallet")
        binding.web.addJavascriptInterface(ADInterface(activity!!, binding.web), ADInterface.NAME)
        binding.web.loadUrl(BuildConfig.WALLET_URI)
    }

    override fun bindView() {
        progressDialog = ProgressDialog(activity)
    }

    @InjectBus(value = EventConstants.START_WALLET_MODE)
    fun event(code: Int) {
        if (code == 0) {
            //打开微信
            binding.web.loadUrl("javascript:facthHomeData(6)")
        } else if (code == 1) {
            //打开任务列表
            binding.web.loadUrl("javascript:gotask()")
        }
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
    fun startPage(code: Int, arg: String) {
        LiveDataBus.getInstance().getChannel(EventConstants.START_PAGE).postValue(code)
    }

    @JavascriptInterface
    fun isLogin(): Boolean {
        return (activity as MainActivity).getUserCenterFragment().isLogin
    }

    @JavascriptInterface
    fun goLogin() {
        activity?.runOnUiThread {
            (activity as MainActivity).getUserCenterFragment().login()
        }
    }

    @JavascriptInterface
    fun isConnected(): Boolean {
        return NetworkUtil.isConnected(activity?.applicationContext)
    }

    @JavascriptInterface
    fun checkRedPoint() {
        LiveDataBus.getInstance().getChannel(EventConstants.UPDATE_RED_POINT).postValue(0)
    }


    @JavascriptInterface
    fun wxLogin2() {
        activity?.runOnUiThread {
            progressDialog?.show()
            WeChatUtil.wxLogin(activity?.applicationContext, object : WXLoginListener {
                override fun onSuccess(code: String?) {
                    binding.web.loadUrl("javascript:wxback2(1)")
                }

                override fun onFail(errMsg: String?) {
                    binding.web.loadUrl("javascript:wxback2(0)")
                }

            })
        }
    }

    @JavascriptInterface
    fun showWeChat(name: String) {
        activity?.runOnUiThread {
            CommonTool.copy(activity!!, name)
            CommonTool.getWeChatApi(activity!!)
        }
    }

    @JavascriptInterface
    fun goUserCenter() {
        activity?.runOnUiThread {
            (activity as MainActivity).showUserCenter()
        }
    }

    override fun onResume() {
        super.onResume()
        if (progressDialog?.isShowing!!) {
            progressDialog?.dismiss()
        }
    }


    override fun onFragmentResume() {
        super.onFragmentResume()
        binding.web.loadUrl("javascript:facthHomeData()")
    }

}
