package gionee.gnservice.app.view.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import com.lee.library.base.BaseFragment
import com.lee.library.widget.refresh.header.DefaultHeader
import gionee.gnservice.app.R
import gionee.gnservice.app.databinding.FragmentVideoChildBinding
import gionee.gnservice.app.view.adapter.VideoAdapter
import gionee.gnservice.app.vm.VideoViewModel
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
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

    private var page = 1
    private val limit = 20

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

            if (page == 1) {
                videoAdapter?.updateData(it.list)
                binding.refresh.setRefreshCompleted()
            } else {
                videoAdapter?.addData(it.list)
            }

            if (page * limit == it.sumLimit) {
                videoAdapter?.loadMoreEnd()
            } else {
                videoAdapter?.loadMoreCompleted()
            }
        })
    }

    override fun bindView() {
        videoAdapter?.setAutoLoadMoreListener {
            viewModel.loadVideos(this.arguments!![key] as String, ++page, limit)
        }

        videoAdapter?.setOnItemClickListener { view, entity, position ->

        }

        binding.refresh.setRefreshCallBack {
            page = 1
            viewModel.loadVideos(this.arguments!![key] as String, page, limit)
        }
    }

    override fun lazyLoad() {
        binding.refresh.autoRefresh()
    }

}
