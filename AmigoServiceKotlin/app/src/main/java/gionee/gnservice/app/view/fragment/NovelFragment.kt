package gionee.gnservice.app.view.fragment

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.Observer
import android.os.Bundle
import com.android.droi.books.BooksLayout
import com.android.droi.books.interfaces.ReaderListener
import com.android.droi.books.interfaces.RequireHandle
import com.android.droi.books.model.bean.CollBookBean
import com.lee.library.base.BaseFragment
import com.lee.library.livedatabus.InjectBus
import com.lee.library.livedatabus.LiveDataBus
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.Constants.Companion.READING_ACTIVITY_NAME
import gionee.gnservice.app.constants.EventConstants
import gionee.gnservice.app.databinding.FragmentNovelBinding
import gionee.gnservice.app.tool.ToastTool
import gionee.gnservice.app.tool.ValueTimer
import gionee.gnservice.app.vm.TimeViewModel

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description 主TAB 小说板块
 */
class NovelFragment :
    BaseFragment<FragmentNovelBinding, TimeViewModel>(R.layout.fragment_novel, TimeViewModel::class.java) {

    private val value by lazy {
        ValueTimer.init(30, object : ValueTimer.TimeCallback {
            override fun endRepeat() {
                viewModel.subAddTime("30", Constants.TYPE_NOVEL, null)
            }
        })
    }

    //统计
    private val requireHandle by lazy {
        RequireHandle(object : ReaderListener {
            override fun onReader(collBookBean: CollBookBean?) {

            }

            override fun onChangePage(collBookBean: CollBookBean?) {
            }

        })
    }

    override fun bindData(savedInstanceState: Bundle?) {
        LiveDataBus.getInstance().injectBus(this)

        viewModel.taskInfo.observe(this, Observer {
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

    override fun bindView() {

    }

    override fun lazyLoad() {
        super.lazyLoad()
        binding.frameContainer.addView(BooksLayout(activity))
        LiveDataBus.getInstance().getChannel(EventConstants.NOVEL_TIMER_STATUS).value = true
    }

    override fun onDestroy() {
        ValueTimer.destroy(value)
        super.onDestroy()
    }

    @InjectBus(value = EventConstants.NOVEL_TIMER_STATUS)
    fun time(run: Boolean) {
        LogUtil.i("inject bus time")
        if (run) {
            ValueTimer.resume(value)
        } else {
            ValueTimer.pause(value)
        }
    }


}