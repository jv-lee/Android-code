package com.lee.navigation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController

import com.lee.navigation.R
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val navController =
            activity?.let { Navigation.findNavController(it, R.id.nav_main_fragment) }

        navController?.let { bottom_nav.setupWithNavController(it) }

//        nav.setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.home -> navController?.navigate(R.id.homeFragment)
//                R.id.video -> navController?.navigate(R.id.videoFragment)
//                R.id.me -> navController?.navigate(R.id.meFragment)
//            }
//            true
//        }
    }

}
