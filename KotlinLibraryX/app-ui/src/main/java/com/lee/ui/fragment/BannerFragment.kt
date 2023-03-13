package com.lee.ui.fragment

import android.widget.ImageView
import com.lee.library.base.BaseFragment
import com.lee.library.extensions.binding
import com.lee.library.extensions.toast
import com.lee.library.widget.banner.holder.CardImageCreateHolder
import com.lee.ui.R
import com.lee.ui.databinding.FragmentBannerBinding

/**
 * 自定义bannerView示范
 * @author jv.lee
 * @date 2021/9/22
 */
class BannerFragment : BaseFragment(R.layout.fragment_banner) {

    private val binding by binding(FragmentBannerBinding::bind)

    private val data = arrayListOf(R.mipmap.header, R.mipmap.header, R.mipmap.header)

    override fun bindView() {
        binding.banner.bindDataCreate(
            data,
            object : CardImageCreateHolder<Int>() {
                override fun bindItem(imageView: ImageView, data: Int) {
                    imageView.setImageResource(data)
                }

                override fun onItemClick(position: Int, item: Int) {
                    toast("item Click $position")
                }
            }
        )
    }

    override fun bindData() {
    }
}