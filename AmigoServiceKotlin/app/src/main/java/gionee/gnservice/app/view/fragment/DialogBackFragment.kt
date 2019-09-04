package gionee.gnservice.app.view.fragment


import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.ViewGroup
import com.lee.library.base.BaseDialogFragment
import com.lee.library.livedatabus.LiveDataBus
import com.lee.library.utils.LogUtil
import com.mobgi.ads.api.AdError
import com.mobgi.ads.api.AdSlot
import com.mobgi.ads.api.ExpressNativeAdData
import com.mobgi.ads.api.MobgiExpressNativeAd
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.EventConstants
import gionee.gnservice.app.databinding.FragmentDialogBackBinding

/**
 * @author jv.lee
 * @date 2019/9/2.
 * @description 退出提示弹窗
 */
class DialogBackFragment :
    BaseDialogFragment<FragmentDialogBackBinding, ViewModel>(R.layout.fragment_dialog_back, null) {

    override fun bindData(savedInstanceState: Bundle?) {
        initNativeAd()
    }

    override fun bindView() {
        binding.btnCancel.setOnClickListener { activity?.finish() }
        binding.btnDefine.setOnClickListener {
            dismiss()
            LiveDataBus.getInstance().getChannel(EventConstants.START_PAGE).value = Constants.MAGNET_WALLET_TASK
        }
    }

    private fun initNativeAd() {
        //创建广告对象
        val mobgiExpressNativeAd = MobgiExpressNativeAd.create(activity)
        // 创建广告加载监听
        val mAdLoadListener = object : MobgiExpressNativeAd.NativeAdLoadListener {
            override fun onAdLoaded(dataList: List<ExpressNativeAdData>?) {
                val size = dataList?.size ?: 0
                LogUtil.i("onAdLoaded: size:$size")
                if (size <= 0) {
                    return
                }
                // 加载成功，展示广告
                // 获取广告数据
                val expressNativeAdData = dataList!![0]
                // 将广告视图添加到页面中
                binding.frameAdContainer.addView(expressNativeAdData.expressNativeAdView)
                // 渲染广告
                expressNativeAdData.render()
            }

            override fun onAdError(adError: AdError) {
                // 加载失败
                LogUtil.e("onAdError: ${adError.errorMsg} code:${adError.errorCode}")
            }

        }

        val adSlot = AdSlot.Builder()
            // 广告位
            .setBlockId("2019081316242480041295")
            .setAdCount(1)
            .setADSize(AdSlot.ADSize(AdSlot.ADSize.FULL_WIDTH, AdSlot.ADSize.AUTO_HEIGHT))
            .build()
        mobgiExpressNativeAd.load(adSlot, mAdLoadListener)
    }

}
