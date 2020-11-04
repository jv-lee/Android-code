package com.lee.behavior.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lee.behavior.R

/**
 * A simple [Fragment] subclass.
 * Use the [ViewBottomBehaviorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewBottomBehaviorFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_bottom_behavior, container, false)
    }

}