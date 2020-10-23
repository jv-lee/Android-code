package com.lee.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.btn_selector_1).setOnClickListener { }
        findViewById<TextView>(R.id.btn_selector_2).setOnClickListener { }
        findViewById<TextView>(R.id.btn_selector_3).setOnClickListener { }
    }


}
