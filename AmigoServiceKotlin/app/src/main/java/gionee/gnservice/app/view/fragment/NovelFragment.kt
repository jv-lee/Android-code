package gionee.gnservice.app.view.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.android.droi.books.BooksLayout
import com.android.droi.books.interfaces.ReaderListener
import com.android.droi.books.interfaces.RequireHandle
import com.android.droi.books.model.bean.CollBookBean
import com.gionee.gnservice.statistics.StatisticsUtil
import com.lee.library.base.BaseFragment
import com.lee.library.livedatabus.InjectBus
import com.lee.library.livedatabus.LiveDataBus
import com.lee.library.utils.ValueTimerEx
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.EventConstants
import gionee.gnservice.app.constants.StatisticsConstants
import gionee.gnservice.app.databinding.FragmentNovelBinding
import gionee.gnservice.app.model.entity.TaskInfo
import gionee.gnservice.app.tool.ToastTool
import gionee.gnservice.app.vm.TimeViewModel

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description 主TAB 小说板块
 */
class NovelFragment :
    BaseFragment<FragmentNovelBinding, TimeViewModel>(R.layout.fragment_novel, TimeViewModel::class.java) {

    private val value by lazy {
        ValueTimerEx.changeRepeat(30 * 1000) { viewModel.subAddTime("30", Constants.TYPE_NOVEL, null) }
    }

    private val observer by lazy {
        Observer<TaskInfo> {
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
        }
    }

    //统计
    private val requireHandle by lazy {
        RequireHandle(object : ReaderListener {
            override fun onReader(collBookBean: CollBookBean?) {
                val map = HashMap<String, Any>()
                map["name"] = collBookBean?.name!!
                map["type"] = collBookBean.category_name
                StatisticsUtil.onEvent(
                    activity?.applicationContext,
                    StatisticsConstants.BOOKS_CLICK,
                    StatisticsConstants.BOOKS_CLICK_LABLE,
                    map
                )
            }

            override fun onChangePage(collBookBean: CollBookBean?) {
                StatisticsUtil.onEvent(activity?.applicationContext, StatisticsConstants.BOOKS_GLIDE)
            }

        })
    }

    override fun bindData(savedInstanceState: Bundle?) {
        LiveDataBus.getInstance().injectBus(this)
        viewModel.taskInfo.observeForever(observer)
    }

    override fun bindView() {

    }

    override fun lazyLoad() {
        super.lazyLoad()
        binding.frameContainer.addView(BooksLayout(activity))
    }

    override fun onDestroy() {
        viewModel.taskInfo.removeObserver(observer)
        ValueTimerEx.destroy(value)
        LiveDataBus.getInstance().unInjectBus(this)
        super.onDestroy()
    }

    @InjectBus(EventConstants.NOVEL_TIMER_STATUS, isActive = false)
    fun event(run: Boolean) {
        if (run) {
            ValueTimerEx.resume(value)
        } else {
            ValueTimerEx.pause(value)
        }
    }

}