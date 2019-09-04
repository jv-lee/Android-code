package gionee.gnservice.app.tool

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.animation.Animation
import android.view.animation.LinearInterpolator

/**
 * @author jv.lee
 * @date 2019/8/19.
 * @description
 */
object ValueTimer {

    fun init(timeCount: Int, call: TimeCallback?): ValueAnimator {
        val value = ValueAnimator.ofInt(1, timeCount)
        value.duration = (timeCount * 1000).toLong()
        value.repeatCount = Animation.INFINITE
        value.interpolator = LinearInterpolator()
        value.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationRepeat(animation: Animator?) {
                call?.endRepeat()
            }
        })
        return value
    }

    fun resume(value: ValueAnimator?) {
        if (value != null) {
            if (!value.isRunning) {
                value.start()
                return
            }
            if (value.isPaused) {
                value.resume()
            }
        }
    }

    fun pause(value: ValueAnimator?) {
        if (value != null) {
            if (value.isRunning) {
                value.pause()
            }
        }
    }

    fun destroy(value: ValueAnimator?) {
        if (value != null) {
            value.pause()
            value.end()
            value.cancel()
        }
    }

    interface TimeCallback {
        fun endRepeat()
    }
}
