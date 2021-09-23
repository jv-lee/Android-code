package com.lee.ui.fragment

import android.widget.ImageView
import com.lee.library.base.BaseFragment
import com.lee.library.extensions.binding
import com.lee.library.extensions.toast
import com.lee.ui.R
import com.lee.ui.databinding.FragmentBannerBinding
import com.lee.ui.widget.create.ImageCreateHolder

/**
 * @author jv.lee
 * @data 2021/9/22
 * @description
 */
class BannerFragment : BaseFragment(R.layout.fragment_banner) {

    private val binding by binding(FragmentBannerBinding::bind)

    private val data = arrayListOf(R.mipmap.header, R.mipmap.header, R.mipmap.header)

    override fun bindView() {
        binding.banner.bindDataCreate(data, object : ImageCreateHolder<Int>() {
            override fun bindItem(imageView: ImageView, data: Int) {
                imageView.setImageResource(data)
            }

            override fun onItemClick(position: Int, item: Int) {
                toast("item Click $position")
            }
        })
    }

    override fun bindData() {

    }

    override fun onResume() {
        super.onResume()
        binding.banner.start()
    }

    override fun onPause() {
        super.onPause()
        binding.banner.pause()
    }

}