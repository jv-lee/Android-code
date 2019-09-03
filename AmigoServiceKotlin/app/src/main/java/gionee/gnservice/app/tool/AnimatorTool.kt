package gionee.gnservice.app.tool

import android.animation.Animator
import android.animation.ValueAnimator

/**
 * @author jv.lee
 * @date 2019/9/3.
 * @description
 */
class AnimatorTool{

    companion object {
        /**
         * 监听时间变化
         *
         * @param time
         * @param listener
         * @param values
         */
        fun timeChange(listener: Animator.AnimatorListener, time: Int, vararg values: Int) {
            val valueAnimator = ValueAnimator.ofInt(*values).setDuration(time.toLong())
            valueAnimator.addListener(listener)
            valueAnimator.start()
        }
    }

}