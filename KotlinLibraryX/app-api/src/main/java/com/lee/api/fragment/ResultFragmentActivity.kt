package com.lee.api.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lee.api.R

class ResultFragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_result)

        supportFragmentManager.beginTransaction().replace(
            R.id.const_container,
            ResultFragment()
        )
            .commit()
    }
}