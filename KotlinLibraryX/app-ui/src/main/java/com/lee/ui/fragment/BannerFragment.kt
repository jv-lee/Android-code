package com.lee.ui.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.lee.library.adapter.binding.ViewBindingAdapter
import com.lee.library.adapter.binding.ViewBindingHolder
import com.lee.library.adapter.item.ViewBindingItem
import com.lee.library.base.BaseFragment
import com.lee.library.extensions.binding
import com.lee.library.extensions.toast
import com.lee.library.widget.banner.holder.CardImageCreateHolder
import com.lee.ui.R
import com.lee.ui.databinding.FragmentBannerBinding
import com.lee.ui.databinding.ItemBannerBinding
import android.util.DisplayMetrics

import androidx.recyclerview.widget.LinearSmoothScroller
import com.lee.library.utils.LogUtil


/**
 * @author jv.lee
 * @date 2021/9/22
 * @description
 */
class BannerFragment : BaseFragment(R.layout.fragment_banner) {

    private val binding by binding(FragmentBannerBinding::bind)

    private val data = arrayListOf(R.mipmap.header, R.mipmap.header, R.mipmap.header)

    private var position = 0

    override fun bindView() {
        binding.banner.bindDataCreate(data, object : CardImageCreateHolder<Int>() {
            override fun bindItem(imageView: ImageView, data: Int) {
                imageView.setImageResource(data)
            }

            override fun onItemClick(position: Int, item: Int) {
                toast("item Click $position")
            }
        })

        binding.rvContainer.layoutManager =
            object : LinearLayoutManager(requireContext(), HORIZONTAL, false) {
                override fun smoothScrollToPosition(
                    recyclerView: RecyclerView,
                    state: RecyclerView.State?,
                    position: Int
                ) {
                    val smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(
                        recyclerView.context
                    ) {
                        // 返回：滑过1px时经历的时间(ms)。
                        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                            return 50f / displayMetrics.densityDpi
                        }
                    }

                    smoothScroller.targetPosition = position
                    startSmoothScroll(smoothScroller)
                }
            }
        binding.rvContainer.adapter = BannerAdapter(requireContext())

        val pagerSnapHelp = PagerSnapHelper()
        pagerSnapHelp.attachToRecyclerView(binding.rvContainer)

        binding.button.setOnClickListener {
            binding.rvContainer.smoothScrollToPosition(++position)
        }
    }

    override fun bindData() {

    }

    private class BannerAdapter(context: Context) :
        ViewBindingAdapter<Int>(
            context,
            arrayListOf(
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header,
                R.mipmap.header
            )
        ) {

        init {
            addItemStyles(BannerItem())
        }

        class BannerItem : ViewBindingItem<Int>() {
            override fun getItemViewBinding(context: Context, parent: ViewGroup): ViewBinding {
                return ItemBannerBinding.inflate(LayoutInflater.from(context), parent, false)
            }

            override fun convert(holder: ViewBindingHolder, entity: Int, position: Int) {
                holder.getViewBinding<ItemBannerBinding>().apply {
                    LogUtil.i("width:${root.width}")
                    ivImage.setImageResource(entity)
                }
            }

        }

    }

}