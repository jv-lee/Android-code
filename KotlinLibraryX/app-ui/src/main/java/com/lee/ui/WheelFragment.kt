package com.lee.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.lee.library.widget.WheelView

/**
 * @author jv.lee
 * @date 2021/1/12
 * @description
 */
class WheelFragment : Fragment(R.layout.fragment_wheel) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wheelView = view.findViewById<WheelView>(R.id.wheel_view)
        wheelView.bindData(arrayListOf<String>().also {
            for (index in 1..10) {
                it.add("Type - $index")
            }
        }, object : WheelView.DataFormat<String> {
            override fun format(item: String) = item
        })
        wheelView.setSelectedListener(object : WheelView.SelectedListener<String> {
            override fun selected(item: String) {
                Log.i("UI", "selected: $item")
            }

        })
    }

}