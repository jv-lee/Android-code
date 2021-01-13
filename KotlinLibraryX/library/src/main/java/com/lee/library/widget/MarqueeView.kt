package com.lee.library.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.lee.library.R
import com.lee.library.extensions.px2sp

/**
 * @author jv.lee
 * @date 2019/5/24.
 * description：跑马灯
 */
class MarqueeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {
    private var data: List<String>? = null
    private var currentIndex = 0
    private var tvContent: TextView? = null
    private var leftDrawable: Drawable? = null
    private var fontColor = 0
    private var fontSize = 0f
    private var mWidth = 0
    private var mHeight = 0
    private var set: AnimatorSet? = null
    private var hasStop = false
    private fun readAttrs(
        context: Context,
        attrs: AttributeSet?
    ) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarqueeView)
        fontColor = typedArray.getColor(R.styleable.MarqueeView_font_color, Color.WHITE)
        fontSize = typedArray.getDimension(R.styleable.MarqueeView_font_size, context.px2sp(16))
        leftDrawable = typedArray.getDrawable(R.styleable.MarqueeView_left_drawable)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.makeMeasureSpec(
            widthMeasureSpec,
            MeasureSpec.UNSPECIFIED
        )
        mHeight = MeasureSpec.makeMeasureSpec(
            heightMeasureSpec,
            MeasureSpec.UNSPECIFIED
        )
        initAnim()
    }

    private fun initView() {
        tvContent = TextView(context).apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            gravity = Gravity.CENTER_VERTICAL
            textSize = context.px2sp(fontSize.toInt())
            setTextColor(fontColor)
            setCompoundDrawables(leftDrawable?.apply {
                setBounds(
                    0,
                    0,
                    minimumWidth,
                    minimumHeight
                )
            }, null, null, null)
        }
        addView(tvContent)
    }

    private fun initAnim() {
        val `in` = ObjectAnimator.ofFloat(
            tvContent,
            View.TRANSLATION_Y,
            mHeight.toFloat(),
            0f
        )
        val out = ObjectAnimator.ofFloat(
            tvContent,
            View.TRANSLATION_Y,
            0f,
            -mHeight.toFloat()
        )
        out.startDelay = 500
        set = AnimatorSet()
        set!!.play(`in`).before(out)
        set!!.duration = 1000
        set!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                tvContent!!.text = data!![currentIndex++]
            }

            override fun onAnimationEnd(animation: Animator) {
                if (!hasStop) {
                    if (currentIndex < data!!.size) {
                        set!!.start()
                    } else {
                        currentIndex = 0
                        set!!.start()
                    }
                }
            }
        })
    }

    fun bindData(data: List<String>?) {
        if (set != null) {
            currentIndex = 0
            pause()
        }
        this.data = data
    }

    fun start() {
        if (data == null || data!!.isEmpty()) {
            return
        }
        hasStop = false
        set!!.start()
    }

    fun pause() {
        hasStop = true
        set!!.end()
    }

    init {
        readAttrs(context, attrs)
        initView()
    }
}