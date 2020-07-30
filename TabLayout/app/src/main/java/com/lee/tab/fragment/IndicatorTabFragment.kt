package com.lee.tab.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.lee.tab.R
import com.lee.tab.adapter.UiPagerAdapter


class IndicatorTabFragment : Fragment() {

    val vpAdapter by lazy {
        UiPagerAdapter(
            childFragmentManager,
            arrayListOf<Fragment>(
                ColorFragment(),
                ColorFragment(),
                ColorFragment(),
                ColorFragment(),
                ColorFragment(),
                ColorFragment(),
                ColorFragment()
            ),
            arrayListOf(
                "tab1",
                "tab2",
                "tab3",
                "tab4",
                "tab5",
                "tab6",
                "tab7"
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_indicator_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tab = view.findViewById<TabLayout>(R.id.tab)
        val vpContainer = view.findViewById<ViewPager>(R.id.vp_container)

        vpContainer.adapter = vpAdapter
        tab.setupWithViewPager(vpContainer)
    }

}