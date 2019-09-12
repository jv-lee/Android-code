package gionee.gnservice.app.view.fragment


import android.arch.lifecycle.Observer
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.dl.infostream.InfoStreamEventListener
import com.dl.infostream.InfoStreamManager
import com.dl.infostream.InfoStreamView
import com.dl.infostream.view.FloatingTimerButton
import com.gionee.gnservice.statistics.StatisticsUtil
import com.lee.library.base.BaseFragment
import com.lee.library.livedatabus.LiveDataBus
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.Cache
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.EventConstants
import gionee.gnservice.app.constants.StatisticsConstants
import gionee.gnservice.app.databinding.FragmentNewsBinding
import gionee.gnservice.app.model.entity.TaskInfo
import gionee.gnservice.app.vm.TimeViewModel

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description 主TAB 新闻板块 - 首页
 */
class NewsFragment :
    BaseFragment<FragmentNewsBinding, TimeViewModel>(R.layout.fragment_news, TimeViewModel::class.java) {
    private var mInfoStreamView: InfoStreamView? = null

    val callback = FloatingTimerButton.CountDownCallBack {
        if (Cache.newsAwardCount >= 4) return@CountDownCallBack
        viewModel.subAddTime("3", Constants.TYPE_NEWS, null)
    }

    val observer by lazy {
        Observer<TaskInfo> {
            if (it?.info != null) {
                LiveDataBus.getInstance().getChannel(EventConstants.NOTIFICATION_AWARD).value =
                    (Cache.ydCount + it.info.getYD)
                Cache.newsAwardCount++
            }
        }
    }

    override fun bindData(savedInstanceState: Bundle?) {
        viewModel.taskInfo.observeForever(observer)

        //设置计时器
        InfoStreamManager.getInstance().setCountDownArgs(
            BitmapFactory.decodeResource(resources, R.mipmap.loading_icon), 5000, 5000, -300, 30
        )

        InfoStreamManager.getInstance().setCountDownFunction(true, callback)

        InfoStreamManager.getInstance().infoStreamEventListener = object : InfoStreamEventListener {
            override fun onNewsClick(
                title: String?,
                url: String?,
                itemId: String?,
                reqId: String?,
                channelCode: String?,
                channel: String?
            ) {
                StatisticsUtil.onEvent(activity?.applicationContext, StatisticsConstants.News_Details_Click)
            }

            override fun onAdClick(
                title: String?,
                url: String?,
                adId: String?,
                reqId: String?,
                channelCode: String?,
                channel: String?
            ) {
                StatisticsUtil.onEvent(activity?.applicationContext, StatisticsConstants.News_Details_Click)
            }

            override fun onScrollEvent(event: String?) {
                StatisticsUtil.onEvent(activity?.applicationContext, StatisticsConstants.News_Slide)
            }

            override fun onInfoStreamTabClick() {
                StatisticsUtil.onEvent(activity?.applicationContext, StatisticsConstants.News_Details_Click)
            }

        }
    }

    override fun bindView() {
    }

    override fun lazyLoad() {
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

    override fun onDestroy() {
        super.onDestroy()
        mInfoStreamView!!.onDestroy()
        viewModel.taskInfo.removeObserver(observer)
    }


}
