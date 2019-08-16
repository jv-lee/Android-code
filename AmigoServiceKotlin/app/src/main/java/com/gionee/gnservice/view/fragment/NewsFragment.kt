package com.gionee.gnservice.view.fragment


import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.widget.FrameLayout
import com.dl.infostream.InfoStreamManager
import com.dl.infostream.InfoStreamView
import com.gionee.gnservice.app.R
import com.lee.library.base.BaseFragment
import com.lee.library.ioc.annotation.ContentView
import kotlinx.android.synthetic.main.fragment_news.*

/**
 * A simple [Fragment] subclass.
 *
 */
@ContentView(R.layout.fragment_news)
class NewsFragment : BaseFragment() {
    private var mInfoStreamView: InfoStreamView? = null

    override fun bindData(savedInstanceState: Bundle?) {
        InfoStreamManager.getInstance()
            .setCountDownArgs(BitmapFactory.decodeResource(resources, R.mipmap.loading_icon), 20000, 5000, 30, 30)
        InfoStreamManager.getInstance().setCountDownFunction(true, {})

        mInfoStreamView = InfoStreamView(mActivity)
        frame_container.addView(
            mInfoStreamView,
            FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
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
