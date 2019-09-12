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
import com.lee.library.widget.WebViewEx
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
import gionee.gnservice.app.view.native.CommonInterface
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
        binding.web.addJavascriptInterface(CommonInterface(activity!!), CommonInterface.NAME)
        binding.web.addJavascriptInterface(ADInterface(activity!!, binding.web), ADInterface.NAME)
        binding.web.addJavascriptInterface(this, "page")
        binding.web.loadUrl(BuildConfig.WALLET_URI)
    }

    override fun bindView() {
        progressDialog = ProgressDialog(activity)

        binding.web.addWebStatusListenerAdapter(object : WebViewEx.WebStatusListenerAdapter() {
            override fun callStart() {
                super.callStart()
                binding.progress.show()
            }

            override fun callFailed() {
                super.callFailed()
                binding.progress.hide()
            }

            override fun callSuccess() {
                super.callSuccess()
                binding.progress.hide()
            }
        })
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

}
