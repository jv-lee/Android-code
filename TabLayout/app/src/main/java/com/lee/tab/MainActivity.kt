package com.lee.tab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lee.tab.fragment.BackgroundIndicatorTabFragment
import com.lee.tab.fragment.IndicatorTabFragment
import com.lee.tab.fragment.SelectBigTextColorFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.frame,
            SelectBigTextColorFragment()
        )
            .commit()
    }
}