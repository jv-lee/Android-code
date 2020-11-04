package com.lee.behavior.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.lee.behavior.R
import com.lee.behavior.R.id.frame_view

/**
 * A simple [Fragment] subclass.
 * Use the [AnimFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnimFragment : Fragment() {

    private val frameView by lazy { view?.findViewById<FrameLayout>(R.id.frame_view) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anim, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        frameView?.setOnClickListener {
            ViewCompat.offsetTopAndBottom(it,-100)
        }
    }

}