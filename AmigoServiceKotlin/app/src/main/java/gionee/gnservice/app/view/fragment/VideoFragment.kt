package gionee.gnservice.app.view.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import com.lee.library.adapter.UiPagerAdapter
import com.lee.library.base.BaseFragment
import com.lee.library.livedatabus.InjectBus
import com.lee.library.livedatabus.LiveDataBus
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.EventConstants
import gionee.gnservice.app.databinding.FragmentVideoBinding
import gionee.gnservice.app.tool.ToastTool
import gionee.gnservice.app.tool.ValueTimer
import gionee.gnservice.app.vm.VideoViewModel
import java.util.ArrayList

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description 主TAB 视频板块
 */
class VideoFragment :
    BaseFragment<FragmentVideoBinding, VideoViewModel>(R.layout.fragment_video, VideoViewModel::class.java) {

    private val vpAdapter by lazy { UiPagerAdapter(childFragmentManager, fragments, titles) }
    private val fragments = ArrayList<Fragment>()
    private val titles = ArrayList<String>()

    private val value by lazy {
        ValueTimer.init(30, object : ValueTimer.TimeCallback {
            override fun endRepeat() {
                viewModel.subAddTime("30", Constants.TYPE_VIDEO, null)
            }
        })
    }

    override fun bindData(savedInstanceState: Bundle?) {
        LiveDataBus.getInstance().injectBus(this)

        binding.vpContainer.adapter = vpAdapter
        binding.vpContainer.offscreenPageLimit = 2
        binding.tab.setupWithViewPager(binding.vpContainer)

        viewModel.apply {
            videoCategory.observe(this@VideoFragment, Observer {
                if (it == null) return@Observer
                for (category in it.cList) {
                    titles.add(category.name)
                    fragments.add(VideoChildFragment.get(category.cid))
                }
                vpAdapter.notifyDataSetChanged()
                binding.vpContainer.offscreenPageLimit = fragments.size - 1
            })

            taskInfo.observe(this@VideoFragment, Observer {
                if (it?.taskOverList != null && it.taskOverList.isNotEmpty()) {
                    for (taskOver in it.taskOverList) {
                        if (taskOver.taskType == 2) {
                            ToastTool.show(
                                activity!!,
                                getString(R.string.task_name, taskOver.taskName, taskOver.taskCount)
                            )
                            LiveDataBus.getInstance().getChannel(EventConstants.UPDATE_RED_POINT).value = 0
                        }
                    }
                }
            })
        }

        viewModel.loadVideoCategory()
    }

    override fun bindView() {

    }

    override fun lazyLoad() {
    }

    override fun onDestroy() {
        ValueTimer.destroy(value)
        super.onDestroy()
    }

    @InjectBus(value = EventConstants.VIDEO_TIMER_STATUS)
    fun event(run: Boolean) {
        if (run) {
            ValueTimer.resume(value)
        } else {
            ValueTimer.pause(value)
        }
    }


}