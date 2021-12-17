package com.lee.library.widget.banner.holder

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import com.lee.library.widget.banner.BannerView

/**
 * @author jv.lee
 * @date 2021/9/23
 * @description
 */
abstract class CardImageCreateHolder<T> : ImageCreateHolder<T>() {

    override fun createView(context: Context): View {
        val cardView = CardView(context)
        cardView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val imageView = ImageView(context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.id = ivID
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        cardView.addView(imageView)
        cardView.radius = 30f
        cardView.useCompatPadding = true
        return cardView
    }

}