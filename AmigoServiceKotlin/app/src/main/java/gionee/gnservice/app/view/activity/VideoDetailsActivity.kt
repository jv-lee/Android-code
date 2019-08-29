package gionee.gnservice.app.view.activity

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.lee.library.base.BaseActivity
import com.lee.library.utils.StatusUtil
import com.lee.library.widget.WebViewEx
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.databinding.ActivityVideoDetailsBinding
import kotlinx.android.synthetic.main.layout_status_toolbar.view.*

class VideoDetailsActivity :
    BaseActivity<ActivityVideoDetailsBinding, ViewModel>(R.layout.activity_video_details, null) {

    private var isOnPause: Boolean = false
    private var url: String? = null

    override fun bindData(savedInstanceState: Bundle?) {
        StatusUtil.setStatusFontLight2(mActivity)
        url = intent.getStringExtra(Constants.URL)
    }

    override fun bindView() {
        binding.include.title.text = "视频"
        binding.include.back.setOnClickListener { finish() }

        binding.web.setWebStatusCallBack(object : WebViewEx.WebStatusCallBack {
            override fun callStart() {
            }

            override fun callSuccess() {
            }

            override fun callFailed() {
            }

            override fun callProgress(progress: Int) {
                if (progress < 90) {
                    binding.include.progress.visibility = View.VISIBLE
                    binding.include.progress.progress = progress
                } else {
                    binding.include.progress.visibility = View.GONE
                }
            }

            override fun callScroll() {
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
        if (isOnPause) {
            binding.web.onResume()
        }
        isOnPause = false
    }

    override fun onPause() {
        super.onPause()
        binding.web.onPause()
        isOnPause = true
    }

    override fun onDestroy() {
        binding.web.clearCache(true)
        binding.web.clearHistory()
        binding.web.visibility = View.GONE
        binding.web.removeAllViews()
        binding.web.destroy()
        isOnPause = false
        super.onDestroy()
    }

}
