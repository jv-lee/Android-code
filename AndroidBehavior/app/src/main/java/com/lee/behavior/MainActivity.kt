package com.lee.behavior

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_view_bottom).setOnClickListener {
            ContainerActivity.start(
                this,
                ContainerActivity.BUILD_VIEW_BOTTOM
            )
        }

        findViewById<Button>(R.id.btn_scroll_header).setOnClickListener {
            ContainerActivity.start(
                this,
                ContainerActivity.BUILD_SCROLL_HEADER
            )
        }
    }
}