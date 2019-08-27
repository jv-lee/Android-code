package gionee.gnservice.app.view.fragment

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.util.Log
import com.lee.library.base.BaseFragment
import com.lee.library.ioc.annotation.ContentView
import com.mobgi.MobgiVideoAd
import gionee.gnservice.app.R
import gionee.gnservice.app.databinding.FragmentVideoBinding

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description
 */
class VideoFragment : BaseFragment<FragmentVideoBinding, ViewModel>(R.layout.fragment_video, null) {

    private val TAG: String = "AManager"

    private var mMobgiVideoAd: MobgiVideoAd? = null

    override fun bindData(savedInstanceState: Bundle?) {
        mMobgiVideoAd = MobgiVideoAd(activity,
            object : MobgiVideoAd.AdListener {

                override fun onAdLoaded() {
                    Log.v(TAG, "onAdLoaded")
                }

                override fun onAdLoadFailed(code: Int, msg: String) {
                    Log.v(TAG, "onAdLoadFailed errorCode = $code msg = $msg")
                }

                override fun onAdDisplayed(blockId: String) {
                    Log.v(TAG, "onAdDisplayed blockId = $blockId")
                }

                override fun onAdClicked(blockId: String) {
                    Log.v(TAG, "onAdClicked blockId = $blockId")
                }

                override fun onAdDismissed(blockId: String, reward: Boolean) {
                    Log.v(TAG, "onAdClicked blockId = $blockId,reward = $reward")
                }

                override fun onAdError(blockId: String, code: Int, message: String) {
                    Log.v(TAG, "onAdError")
                }
            })
    }

    override fun bindView() {
        binding.btn.setOnClickListener {
            if (mMobgiVideoAd!!.isReady("2019041911062079551052")) {
                mMobgiVideoAd!!.show(activity, "2019041911062079551052")
            }

        }
    }


}