package com.lee.ui.fragment

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.lee.library.base.BaseFragment
import com.lee.library.extensions.binding
import com.lee.ui.R
import com.lee.ui.databinding.FragmentBannerBinding
import com.lee.ui.utils.moveToItem

/**
 * @author jv.lee
 * @data 2021/9/22
 * @description
 */
class BannerFragment : BaseFragment(R.layout.fragment_banner) {

    private val binding by binding(FragmentBannerBinding::bind)
    private val mAdapter by lazy { BannerAdapter() }

    private val mHandler by lazy { Handler(Looper.getMainLooper()) }
    private val runnable = object : Runnable {
        override fun run() {
            var itemIndex = binding.vpContainer.currentItem
            binding.vpContainer.currentItem = ++itemIndex
            binding.vpContainer.moveToItem(++itemIndex, 500)
            mHandler.postDelayed(this, 3000)
        }
    }

    override fun bindView() {
        binding.vpContainer.adapter = mAdapter
        binding.vpContainer.setCurrentItem(mAdapter.getStartSelectItem(), false)
        mHandler.postDelayed(runnable, 3000)
    }

    override fun bindData() {

    }

    class BannerAdapter : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
        private val mLooperCountFactor = 500
        private val dataSize = 3

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
            val iv = ImageView(parent.context)
            iv.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            iv.scaleType = ImageView.ScaleType.CENTER_CROP
            return BannerViewHolder(iv)
        }

        override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
            (holder.itemView as ImageView).setImageResource(R.mipmap.header)
        }

        override fun getItemCount(): Int {
            return mLooperCountFactor * 3
        }

        fun getStartSelectItem(): Int {
            // 我们设置当前选中的位置为Integer.MAX_VALUE / 2,这样开始就能往左滑动
            // 但是要保证这个值与getRealPosition 的 余数为0，因为要从第一页开始显示
            var currentItem: Int = dataSize * mLooperCountFactor / 2
            if (currentItem % dataSize == 0) {
                return currentItem
            }
            // 直到找到从0开始的位置
            while (currentItem % dataSize != 0) {
                currentItem++
            }
            return currentItem
        }

        class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }


}