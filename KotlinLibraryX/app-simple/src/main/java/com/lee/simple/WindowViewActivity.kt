package com.lee.simple

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.lee.library.extensions.toast

class WindowViewActivity : AppCompatActivity() {

    private val resultLauncher = WindowPermissionLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window_view)

        findViewById<Button>(R.id.button).setOnClickListener {
            resultLauncher.checkOverlayPermission(this, callback = {
                toast("show Window")
            }, notPermission = {
                toast("not Permission")
            })
        }
    }


}