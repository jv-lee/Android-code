package com.lee.behavior.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lee.behavior.R

/**
 * A simple [Fragment] subclass.
 * Use the [ScrollHeaderBehaviorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScrollHeaderBehaviorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scroll_header_behavior, container, false)
    }

}