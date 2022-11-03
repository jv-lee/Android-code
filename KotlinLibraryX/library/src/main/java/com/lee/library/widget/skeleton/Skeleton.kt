package com.lee.library.widget.skeleton

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.view.contains

/**
 * 骨架屏幕构建器
 * @author jv.lee
 * @date 2021/10/29
 */
class Skeleton private constructor(builder: Builder) {

    private var shimmerLayout: ShimmerLayout
    private var viewGroup: ViewGroup = builder.view as ViewGroup

    init {
        shimmerLayout = ShimmerLayout(viewGroup.context)
        shimmerLayout.layoutParams = viewGroup.layoutParams

        builder.loadRes?.let(this::bindSkeletonLayout)
        builder.color?.let(shimmerLayout::setShimmerColor)
        builder.duration?.let(shimmerLayout::setShimmerAnimationDuration)
        builder.angle?.let(shimmerLayout::setShimmerAngle)
        builder.maskWidth?.let(shimmerLayout::setMaskWidth)
        builder.gradientColorWidth?.let(shimmerLayout::setGradientCenterColorWidth)
        builder.reverseAnimation?.let(shimmerLayout::setAnimationReversed)
    }

    private fun bindSkeletonLayout(layoutRes: Int) {
        LayoutInflater.from(viewGroup.context).inflate(layoutRes, shimmerLayout, true)
    }

    fun bind() {
        if (!viewGroup.contains(shimmerLayout)) {
            viewGroup.addView(shimmerLayout)
        }

        shimmerLayout.startShimmerAnimation()
    }

    fun unBind() {
        shimmerLayout.stopShimmerAnimation()

        if (viewGroup.contains(shimmerLayout)) {
            viewGroup.removeView(shimmerLayout)
        }
    }

    class Builder(internal var view: View) {
        internal var loadRes: Int? = null
        internal var color: Int? = null
        internal var duration: Int? = null
        internal var angle: Int? = null
        internal var maskWidth: Float? = null
        internal var gradientColorWidth: Float? = null
        internal var reverseAnimation: Boolean? = null

        fun load(@LayoutRes layoutRes: Int): Builder {
            this.loadRes = layoutRes
            return this
        }

        fun color(@ColorRes color: Int): Builder {
            this.color = color
            return this
        }

        fun duration(duration: Int): Builder {
            this.duration = duration
            return this
        }

        fun angle(angle: Int): Builder {
            this.angle = angle
            return this
        }

        fun maskWidth(maskWidth: Float): Builder {
            this.maskWidth = maskWidth
            return this
        }

        fun gradientColorWidth(gradientColorWidth: Float): Builder {
            this.gradientColorWidth = gradientColorWidth
            return this
        }

        fun reverseAnimation(reverseAnimation: Boolean): Builder {
            this.reverseAnimation = reverseAnimation
            return this
        }

        fun bind(): Skeleton {
            val skeleton = Skeleton(this)
            skeleton.bind()
            return skeleton
        }
    }
}