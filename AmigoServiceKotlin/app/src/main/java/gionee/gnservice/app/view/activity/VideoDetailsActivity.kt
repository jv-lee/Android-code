package gionee.gnservice.app.view.activity

import android.animation.ValueAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.lee.library.base.BaseActivity
import com.lee.library.livedatabus.LiveDataBus
import com.lee.library.utils.StatusUtil
import com.lee.library.widget.WebViewEx
import gionee.gnservice.app.Cache
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.EventConstants
import gionee.gnservice.app.databinding.ActivityVideoDetailsBinding
import gionee.gnservice.app.tool.ValueTimer
import gionee.gnservice.app.vm.VideoDetailsViewModel
import kotlinx.android.synthetic.main.layout_status_toolbar.view.*

/**
 * @author jv.lee
 * @date 2019/8/16.
 * @description 视频详情页面
 */
class VideoDetailsActivity :
    BaseActivity<ActivityVideoDetailsBinding, VideoDetailsViewModel>(
        R.layout.activity_video_details,
        VideoDetailsViewModel::class.java
    ) {

    private var url: String? = null
    private var value: ValueAnimator? = null

    override fun bindData(savedInstanceState: Bundle?) {
        StatusUtil.setStatusFontLight2(mActivity)
        url = intent.getStringExtra(Constants.URL)

        viewModel.award.observe(this, Observer {
            LiveDataBus.getInstance().getChannel(EventConstants.NOTIFICATION_AWARD).value = (Cache.ydCount + it!!.count)
        })
    }

    override fun bindView() {
        binding.include.title.text = "视频"
        binding.include.back.setOnClickListener { finish() }

        binding.web.addWebStatusListenerAdapter(object : WebViewEx.WebStatusListenerAdapter() {
            override fun callProgress(progress: Int) {
                if (progress < 90) {
                    binding.include.progress.visibility = View.VISIBLE
                    binding.include.progress.progress = progress
                } else {
                    binding.include.progress.visibility = View.GONE
                }
            }
        })
        binding.web.loadUrl(url)

        viewModel.getVideoAward(1)
        value = ValueTimer.init(12, object : ValueTimer.TimeCallback {
            override fun endRepeat() {
                viewModel.getVideoAward(2)
            }
        })
        value?.start()
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
        ValueTimer.resume(value)
    }

    override fun onPause() {
        super.onPause()
        binding.web.exPause()
        ValueTimer.pause(value)
    }

    override fun onDestroy() {
        binding.web.exDestroy()
        ValueTimer.destroy(value)
        super.onDestroy()
    }

}
