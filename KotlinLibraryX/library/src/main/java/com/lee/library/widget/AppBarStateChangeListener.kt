package com.lee.library.widget

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

/**
 * appBarLayout.addOnOffsetChangedListener
 * @author jv.lee
 * @date 2020/8/3
 */
abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {
    enum class State {
        EXPANDED, COLLAPSED, IDLE
    }

    private var mCurrentState = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        when {
            verticalOffset == 0 -> {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED)
                }
                mCurrentState = State.EXPANDED
            }
            abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED)
                }
                mCurrentState = State.COLLAPSED
            }
            else -> {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE)
                }
                mCurrentState = State.IDLE
            }
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: State)
}