package com.lee.tab.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.lee.tab.R

class ColorFragment : Fragment() {

    private val colors = arrayListOf<Int>(
        android.R.color.black,
        android.R.color.holo_blue_bright,
        android.R.color.holo_green_dark,
        android.R.color.holo_red_dark,
        android.R.color.holo_orange_dark
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<FrameLayout>(R.id.frame)?.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                colors[(0..4).random()]
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_color, container, false)
    }

}