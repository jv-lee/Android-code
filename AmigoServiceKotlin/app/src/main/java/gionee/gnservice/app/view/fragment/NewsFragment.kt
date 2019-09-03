package gionee.gnservice.app.view.fragment


import android.arch.lifecycle.ViewModel
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.dl.infostream.InfoStreamManager
import com.dl.infostream.InfoStreamView
import com.lee.library.base.BaseFragment
import gionee.gnservice.app.R
import gionee.gnservice.app.databinding.FragmentNewsBinding

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description 主TAB 新闻板块 - 首页
 */
class NewsFragment : BaseFragment<FragmentNewsBinding, ViewModel>(R.layout.fragment_news, null) {
    private var mInfoStreamView: InfoStreamView? = null

    override fun bindData(savedInstanceState: Bundle?) {
        InfoStreamManager.getInstance().setCountDownArgs(
            BitmapFactory.decodeResource(resources, R.mipmap.loading_icon),
            20000,
            5000,
            30,
            30
        )
        InfoStreamManager.getInstance().setCountDownFunction(true, {})

        mInfoStreamView = InfoStreamView(activity)
        binding.frameContainer.addView(
            mInfoStreamView,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        mInfoStreamView!!.setScreenMode(true)
    }

    override fun bindView() {
    }

    override fun onDestroy() {
        super.onDestroy()
        mInfoStreamView!!.onDestroy()
    }


}
