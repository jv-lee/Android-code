package com.lee.library.widget.banner.holder

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import com.lee.library.widget.banner.BannerView

/**
 * imageView banner样式
 * @author jv.lee
 * @date 2021/9/23
 */
abstract class ImageCreateHolder<T> : BannerView.CreateHolder<T> {
    protected val ivID = ViewCompat.generateViewId()
    override fun createView(context: Context): View {
        val imageView = ImageView(context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.id = ivID
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return imageView
    }

    override fun onBind(
        holder: BannerView.BannerAdapter<T>.BannerViewHolder,
        position: Int,
        item: T
    ) {
        val imageView = holder.itemView.findViewById<ImageView>(ivID)
        bindItem(imageView, item)
    }

    abstract fun bindItem(imageView: ImageView, data: T)
}