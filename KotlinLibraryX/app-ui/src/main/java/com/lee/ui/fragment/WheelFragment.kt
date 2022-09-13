package com.lee.ui.fragment

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lee.library.base.BaseFragment
import com.lee.library.extensions.binding
import com.lee.library.extensions.dp2px
import com.lee.library.widget.WheelView
import com.lee.ui.R
import com.lee.ui.databinding.FragmentWheelBinding
import com.lee.ui.utils.BlurUtils
import kotlin.math.abs

/**
 *
 * @author jv.lee
 * @date 2021/1/12
 */
class WheelFragment : BaseFragment(R.layout.fragment_wheel) {

    private val binding by binding(FragmentWheelBinding::bind)

    private val TAG = "wheel_touch"

    override fun bindView() {
        initBlurImage()
        initRefresh()
        initWheel()
    }

    override fun bindData() {

    }

    private fun initBlurImage() {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.header)
        val blurBitmap = BlurUtils.blur(requireContext(), bitmap, 15f)

        binding.ivImage.setImageBitmap(blurBitmap)
    }

    private fun initRefresh() {
        binding.refresh.dropLayout(binding.constRoot)
        binding.refresh.setOnRefreshListener {
            binding.refresh.postDelayed({ binding.refresh.dropLayoutEnd(binding.constRoot) }, 1000)
        }
    }

    private fun initWheel() {
        binding.wheelView.bindData(arrayListOf<String>().also {
            for (index in 1..10) {
                it.add("Type - $index")
            }
        }, object : WheelView.DataFormat<String> {
            override fun format(item: String) = item
        },object : WheelView.SelectedListener<String> {
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
                    e1: MotionEvent,
                    e2: MotionEvent,
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