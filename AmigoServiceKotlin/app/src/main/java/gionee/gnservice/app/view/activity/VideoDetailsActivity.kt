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

    private var url: String? = null

    override fun bindData(savedInstanceState: Bundle?) {
        StatusUtil.setStatusFontLight2(mActivity)
        url = intent.getStringExtra(Constants.URL)
    }

    override fun bindView() {
        binding.include.title.text = "视频"
        binding.include.back.setOnClickListener { finish() }

        binding.web.addWebStatusListenerAdapter(object:WebViewEx.WebStatusListenerAdapter(){
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

    override fun onDestroy() {
        binding.web.exDestroy()
        super.onDestroy()
    }

}
