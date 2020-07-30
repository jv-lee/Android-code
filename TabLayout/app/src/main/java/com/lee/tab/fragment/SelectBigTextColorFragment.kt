package com.lee.tab.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.lee.tab.R
import com.lee.tab.adapter.UiPagerAdapter
import com.lee.tab.bindTab
import com.lee.tab.bindViewPager


class SelectBigTextColorFragment : Fragment() {

    private val tabList = arrayListOf("Update", "Completed")
    private val fragments = listOf(
        ColorFragment(),
        ColorFragment()
    )
    private var updateColor: Int? = null
    private var selectColor: Int? = null
    private var unSelectColor: Int? = null
    private val vpAdapter by lazy {
        UiPagerAdapter(
            childFragmentManager,
            fragments,
            tabList
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateColor = ContextCompat.getColor(activity!!,
            R.color.colorAccent
        )
        selectColor = ContextCompat.getColor(activity!!,
            R.color.colorPrimary
        )
        unSelectColor = ContextCompat.getColor(activity!!,
            R.color.colorAccent
        )

        val tabLayout = view.findViewById<TabLayout>(R.id.tab)
        val vpContainer = view.findViewById<ViewPager>(R.id.vp_container)

        tabLayout.bindViewPager(vpContainer!!)
        tabLayout.bindTab(tabList,
            {
                getTabView(it)
            },
            { tab: TabLayout.Tab, isSelect: Boolean ->
                updateTabTextView(tab, isSelect)
            })
        vpContainer.adapter = vpAdapter
        vpContainer.setCurrentItem(1, false)

        //直接替换颜色
        view.findViewById<Button>(R.id.btn).setOnClickListener {
            selectColor = ContextCompat.getColor(activity!!,
                R.color.colorPrimaryDark
            )
            unSelectColor = ContextCompat.getColor(activity!!,
                R.color.colorPrimaryDark
            )
            updateTabTextView(tabLayout.getTabAt(0)!!, false)
            updateTabTextView(tabLayout.getTabAt(1)!!, true)
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_big_text_color, container, false)
    }

    private fun getTabView(currentPosition: Int): View {
        val view: View = LayoutInflater.from(activity).inflate(R.layout.tab_item, null)
        val textView = view.findViewById(R.id.tab_item_textview) as TextView
        textView.text = tabList[currentPosition]
        return view
    }

    private fun updateTabTextView(tab: TabLayout.Tab, isSelect: Boolean) {
        val textView = tab.customView!!.findViewById<View>(R.id.tab_item_textview) as TextView
        if (isSelect) {
            textView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22F)
            textView.setTextColor(if (tab.position == 0) updateColor!! else selectColor!!)
            textView.text = tab.text
        } else {
            textView.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
            textView.setTextColor(if (tab.position == 1) updateColor!! else unSelectColor!!)
            textView.text = tab.text
        }
    }

}