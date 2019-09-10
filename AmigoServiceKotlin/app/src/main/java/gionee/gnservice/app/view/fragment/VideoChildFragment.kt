package gionee.gnservice.app.view.fragment

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lee.library.base.BaseFragment
import com.lee.library.widget.refresh.header.DefaultHeader
import gionee.gnservice.app.Cache.Companion.limit
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.databinding.FragmentVideoChildBinding
import gionee.gnservice.app.view.activity.VideoDetailsActivity
import gionee.gnservice.app.view.adapter.VideoAdapter
import gionee.gnservice.app.vm.VideoViewModel
import java.util.*

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description 主TAB 视频板块 - 子页面
 */
class VideoChildFragment :
    BaseFragment<FragmentVideoChildBinding, VideoViewModel>(R.layout.fragment_video_child, VideoViewModel::class.java) {

    companion object {
        const val key: String = "cid"

        fun get(cid: String): VideoChildFragment {
            val bundle = Bundle()
            bundle.putString(key, cid)
            val fragment = VideoChildFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var videoAdapter: VideoAdapter? = null

    override fun bindData(savedInstanceState: Bundle?) {
        videoAdapter = VideoAdapter(context!!, ArrayList())
        videoAdapter?.openLoadMore()

        binding.rvContainer.layoutManager = LinearLayoutManager(activity)
        binding.rvContainer.adapter = videoAdapter?.proxy

        binding.refresh.setBootView(
            binding.frameVideoChildContainer,
            binding.rvContainer,
            DefaultHeader(activity),
            null
        )

        viewModel.videoLists.observe(this, Observer {
            if (it == null || it.list.isEmpty()) return@Observer

            if (it.page == 1) {
                videoAdapter?.updateData(it.list)
                binding.refresh.setRefreshCompleted()
            } else {
                videoAdapter?.addData(it.list)
            }

            if (it.page * limit == it.sumLimit) {
                videoAdapter?.loadMoreEnd()
            } else {
                videoAdapter?.loadMoreCompleted()
            }
        })
    }

    override fun bindView() {
        videoAdapter?.setAutoLoadMoreListener {
            viewModel.loadVideos(this.arguments!![key] as String)
        }

        videoAdapter?.setOnItemClickListener { view, entity, position ->
            startActivity(
                Intent(activity, VideoDetailsActivity::class.java)
                    .putExtra(Constants.URL, entity.shareLink)
            )
        }

        binding.refresh.setRefreshCallBack {
            viewModel.refreshVideos(this.arguments!![key] as String)
        }
    }

    override fun lazyLoad() {
        binding.refresh.autoRefresh()
    }

}
