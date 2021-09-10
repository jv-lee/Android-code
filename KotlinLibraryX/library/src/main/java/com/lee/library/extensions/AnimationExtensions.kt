package com.lee.library.extensions

import android.view.animation.Animation

/**
 * @author jv.lee
 * @data 2021/9/10
 * @description
 */
abstract class AnimationAdapter : Animation.AnimationListener {
    override fun onAnimationStart(animation: Animation?) {
    }

    override fun onAnimationEnd(animation: Animation?) {

    }

    override fun onAnimationRepeat(animation: Animation?) {

    }
}

inline fun Animation.endListener(crossinline call: () -> Unit) {
    setAnimationListener(object : AnimationAdapter() {
        override fun onAnimationEnd(animation: Animation?) {
            call()
        }
    })
}

inline fun Animation.startListener(crossinline call: () -> Unit) {

    setAnimationListener(object : AnimationAdapter() {
        override fun onAnimationStart(animation: Animation?) {
            call()
        }
    })
}