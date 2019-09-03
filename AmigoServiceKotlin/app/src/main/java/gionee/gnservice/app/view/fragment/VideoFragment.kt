package gionee.gnservice.app.view.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import com.lee.library.adapter.UiPagerAdapter
import com.lee.library.base.BaseFragment
import gionee.gnservice.app.R
import gionee.gnservice.app.databinding.FragmentVideoBinding
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

    override fun bindData(savedInstanceState: Bundle?) {
        binding.vpContainer.adapter = vpAdapter
        binding.vpContainer.offscreenPageLimit = 2
        binding.tab.setupWithViewPager(binding.vpContainer)

        viewModel.videoCategory.observe(this@VideoFragment, Observer {
            if (it == null) return@Observer
            for (category in it.cList) {
                titles.add(category.name)
                fragments.add(VideoChildFragment.get(category.cid))
            }
            vpAdapter.notifyDataSetChanged()
            binding.vpContainer.offscreenPageLimit = fragments.size - 1
        })
        viewModel.loadVideoCategory()
    }

    override fun bindView() {

    }

    override fun lazyLoad() {
    }


}