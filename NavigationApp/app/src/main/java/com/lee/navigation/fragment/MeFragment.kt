package com.lee.navigation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lee.navigation.MainActivity
import com.lee.navigation.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_me.*

/**
 * A simple [Fragment] subclass.
 */
class MeFragment : Fragment() {

    var currentNumber = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_start_details.setOnClickListener {
            findNavController().navigate(R.id.action_meFragment_to_detailsFragment)
            (activity as MainActivity).hide()
        }
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(context, "this is Number:${++currentNumber}", Toast.LENGTH_SHORT).show()
        (activity as MainActivity).show()
    }

}
