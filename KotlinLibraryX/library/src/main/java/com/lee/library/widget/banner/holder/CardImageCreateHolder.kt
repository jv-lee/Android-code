package com.lee.library.widget.banner.holder

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel

/**
 * @author jv.lee
 * @date 2021/9/23
 *
 */
abstract class CardImageCreateHolder<T> : ImageCreateHolder<T>() {

    override fun createView(context: Context): View {
        val radius = 30f

        val cardView = CardView(context)
        cardView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        cardView.radius = radius
        cardView.useCompatPadding = true

        val imageView = ShapeableImageView(context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.shapeAppearanceModel = ShapeAppearanceModel.Builder()
            .setAllCornerSizes(radius)
            .build()
        imageView.id = ivID
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        return cardView.apply {
            addView(imageView)
        }
    }

}