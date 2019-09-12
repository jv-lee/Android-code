package gionee.gnservice.app.view.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.lee.library.base.BaseActivity
import com.lee.library.livedatabus.LiveDataBus
import com.lee.library.utils.StatusUtil
import com.lee.library.utils.TimerEx
import com.lee.library.widget.WebViewEx
import gionee.gnservice.app.Cache
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.EventConstants
import gionee.gnservice.app.databinding.ActivityVideoDetailsBinding
import gionee.gnservice.app.vm.VideoDetailsViewModel
import kotlinx.android.synthetic.main.layout_status_toolbar.view.*
import java.util.concurrent.ScheduledFuture

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

    var run: ScheduledFuture<*>? = null
    private var url: String? = null
    private var isFirst = true
    private var hasAward = true

    override fun bindData(savedInstanceState: Bundle?) {
        StatusUtil.setStatusFontLight2(mActivity)
        url = intent.getStringExtra(Constants.URL)

        viewModel.award.observe(this, Observer {
            if (!isDestroyed) {
                LiveDataBus.getInstance().getChannel(EventConstants.NOTIFICATION_AWARD).value =
                    (Cache.ydCount + it!!.count)
            }
        })

        viewModel.hasAward.observe(this, Observer {
            hasAward = it!!
        })

        viewModel.videoAwardEnable(url!!)
    }

    override fun bindView() {
        binding.include.title.text = "视频"
        binding.include.back.setOnClickListener { finish() }

        binding.web.addWebStatusListenerAdapter(object : WebViewEx.WebStatusListenerAdapter() {

            override fun callSuccess() {
                //首次打开奖励
                if (isFirst && hasAward) {
                    isFirst = false
                    //进入奖励
                    viewModel.getVideoAward(1)

                    //延迟获取
                    run = TimerEx.get().run({ viewModel.getVideoAward(2) }, 12)
                }
            }

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

    override fun finish() {
        super.finish()
        run?.cancel(true)
    }

    override fun onDestroy() {
        binding.web.exDestroy()
        super.onDestroy()
    }

}
