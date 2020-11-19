package com.lee.ui

import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lee.library.widget.SnackBarEx


class MainActivity : AppCompatActivity() {

    private val scrollRoot by lazy { findViewById<ScrollView>(R.id.scroll_root) }
    private val snackBar by lazy {
        SnackBarEx.Builder(scrollRoot)
            .setDuration(5000)
            .setActionText("action")
            .setMessage("message")
            .setOnClickListener {  }
            .build()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.btn_selector_1).setOnClickListener {
            snackBar.show()
        }
        findViewById<TextView>(R.id.btn_selector_2).setOnClickListener { }
        findViewById<TextView>(R.id.btn_selector_3).setOnClickListener { }
    }


}
