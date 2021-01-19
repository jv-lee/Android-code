package com.lee.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lee.library.widget.nav.NumberDotView

/**
 * @author jv.lee
 * @date 2021/1/14
 * @description
 */
class DotFragment : Fragment(R.layout.fragment_dot) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<NumberDotView>(R.id.numberDotView).setNumber(12)
    }
}