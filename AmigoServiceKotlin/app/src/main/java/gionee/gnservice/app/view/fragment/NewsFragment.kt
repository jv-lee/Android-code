package gionee.gnservice.app.view.fragment


import android.arch.lifecycle.Observer
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.dl.infostream.InfoStreamEventListener
import com.dl.infostream.InfoStreamManager
import com.dl.infostream.InfoStreamView
import com.lee.library.base.BaseFragment
import com.lee.library.livedatabus.LiveDataBus
import gionee.gnservice.app.Cache
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.EventConstants
import gionee.gnservice.app.databinding.FragmentNewsBinding
import gionee.gnservice.app.tool.ToastTool
import gionee.gnservice.app.vm.TimeViewModel

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description 主TAB 新闻板块 - 首页
 */
class NewsFragment :
    BaseFragment<FragmentNewsBinding, TimeViewModel>(R.layout.fragment_news, TimeViewModel::class.java) {
    private var mInfoStreamView: InfoStreamView? = null

    override fun bindData(savedInstanceState: Bundle?) {
        viewModel.taskInfo.observe(this, Observer {
            if (it?.info != null) {
                LiveDataBus.getInstance().getChannel(EventConstants.NOTIFICATION_AWARD).value =
                    (Cache.ydCount + it.info.getYD)
            }
        })

        //设置计时器
        InfoStreamManager.getInstance()
            .setCountDownArgs(BitmapFactory.decodeResource(resources, R.mipmap.loading_icon), 5000, 5000, -300, 30)
        InfoStreamManager.getInstance().setCountDownFunction(true) {
            viewModel.subAddTime("3", Constants.TYPE_NEWS, null)
        }

        InfoStreamManager.getInstance().infoStreamEventListener = object : InfoStreamEventListener {
            override fun onNewsClick(p0: String?, p1: String?, p2: String?, p3: String?, p4: String?, p5: String?) {
            }

            override fun onAdClick(p0: String?, p1: String?, p2: String?, p3: String?, p4: String?, p5: String?) {
            }

            override fun onScrollEvent(p0: String?) {
            }

            override fun onInfoStreamTabClick() {
            }

        }

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
