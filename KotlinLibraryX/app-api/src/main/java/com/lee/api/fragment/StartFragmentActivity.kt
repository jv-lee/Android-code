package com.lee.api.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lee.api.R

class StartFragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_fragment)

        supportFragmentManager.beginTransaction().replace(
            R.id.const_container,
            StartFragment()
        )
            .commit()
    }
}