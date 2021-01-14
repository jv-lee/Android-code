package com.lee.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.lee.library.utils.StatusUtil

/**
 * @author jv.lee
 * @date 2021/1/14
 * @description
 */
class TestFragment : Fragment(R.layout.fragment_test) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rootView = view.findViewById<FrameLayout>(R.id.root_container)
        rootView.addView(View(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                StatusUtil.getStatusBarHeight(requireContext())
            )
            setBackgroundColor(Color.BLACK)
        })
    }
}