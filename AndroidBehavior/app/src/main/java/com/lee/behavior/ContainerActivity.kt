package com.lee.behavior

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lee.behavior.fragment.ScrollHeaderBehaviorFragment
import com.lee.behavior.fragment.ViewBottomBehaviorFragment

class ContainerActivity : AppCompatActivity() {

    companion object {
        const val BUILD_TAG = "build_tag"
        const val BUILD_VIEW_BOTTOM = 1
        const val BUILD_SCROLL_HEADER = 2

        fun start(context: Context, tag: Int) {
            context.startActivity(
                Intent(context, ContainerActivity::class.java).putExtra(
                    BUILD_TAG,
                    tag
                )
            )
        }
    }

    private val tag by lazy { intent.getIntExtra(BUILD_TAG, 1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, buildFragmentByTag()).commit()
    }

    private fun buildFragmentByTag(): Fragment {
        return when (tag) {
            BUILD_VIEW_BOTTOM -> ViewBottomBehaviorFragment()
            BUILD_SCROLL_HEADER-> ScrollHeaderBehaviorFragment()
            else -> ViewBottomBehaviorFragment()
        }
    }
}