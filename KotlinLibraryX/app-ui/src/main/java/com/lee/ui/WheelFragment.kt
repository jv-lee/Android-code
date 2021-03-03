package com.lee.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lee.library.extensions.dp2px
import com.lee.library.widget.WheelView
import kotlin.math.abs

/**
 * @author jv.lee
 * @date 2021/1/12
 * @description
 */
class WheelFragment : Fragment(R.layout.fragment_wheel) {

    private val TAG = "wheel_touch"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBlurImage(view)
        initRefresh(view)
        initWheel(view)
    }

    private fun initBlurImage(view: View) {
        val ivImage = view.findViewById<ImageView>(R.id.iv_image)
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.header)
        val blurBitmap = BlurUtils.blur(requireContext(), bitmap, 15f)

        ivImage.setImageBitmap(blurBitmap)
    }

    private fun initRefresh(view: View) {
        val refreshView = view.findViewById<SwipeRefreshLayout>(R.id.refresh)
        val constRoot = view.findViewById<ConstraintLayout>(R.id.const_root)
        refreshView.dropLayout(constRoot)
        refreshView.setOnRefreshListener {
            refreshView.postDelayed({ refreshView.dropLayoutEnd(constRoot) }, 1000)
        }
    }

    private fun initWheel(view: View) {
        val wheelView = view.findViewById<WheelView>(R.id.wheel_view)
        wheelView.bindData(arrayListOf<String>().also {
            for (index in 1..10) {
                it.add("Type - $index")
            }
        }, object : WheelView.DataFormat<String> {
            override fun format(item: String) = item
        })
        wheelView.setSelectedListener(object : WheelView.SelectedListener<String> {
            override fun selected(item: String) {
                Log.i("UI", "selected: $item")
            }

        })
    }

    private fun SwipeRefreshLayout.dropLayoutEnd(contentView: View) {
        isRefreshing = false
        val animator = ValueAnimator.ofFloat(contentView.translationY, 0f)
        animator.duration = 100
        animator.addUpdateListener {
            contentView.translationY = it.animatedValue as Float
        }
        animator.start()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun SwipeRefreshLayout.dropLayout(contentView: View) {
        var hasDrop = false
        val gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onScroll(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    distanceX: Float,
                    distanceY: Float
                ): Boolean {
                    if (contentView.translationY <= requireContext().dp2px(146) &&
                        contentView.translationY >= 0f &&
                        (contentView.translationY == 0f && distanceY < 0)
                    ) {
                        contentView.translationY += negate(distanceY) / 2
                        Log.i(TAG, "onScroll: ${contentView.translationY}")
                    }
                    return super.onScroll(e1, e2, distanceX, distanceY)
                }
            })
        setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (hasDrop) {
                    val animator =
                        ValueAnimator.ofFloat(contentView.translationY, requireContext().dp2px(76))
                    animator.duration = 200
                    animator.addUpdateListener {
                        contentView.translationY = it.animatedValue as Float
                    }
                    animator.start()
                }
                hasDrop = false
            }
            gestureDetector.onTouchEvent(event)
            false
        }
    }

    private fun negate(number: Float): Float {
        return when {
            number > 0 -> {
                return -number
            }
            number < 0 -> {
                return abs(number)
            }
            else -> {
                number
            }
        }
    }

}