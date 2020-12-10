package com.lee.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lee.library.dialog.LoadingDialog
import com.lee.library.utils.TextSpanHelper
import com.lee.library.widget.SnackBarEx
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val scrollRoot by lazy { findViewById<ScrollView>(R.id.scroll_root) }
    private val snackBar by lazy {
        SnackBarEx.Builder(scrollRoot)
            .setDuration(5000)
            .setActionText("action")
            .setMessage("message")
            .setOnClickListener { }
            .build()
    }

    private val loadingDialog by lazy { LoadingDialog(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<TextView>(R.id.btn_selector_1).setOnClickListener {
            snackBar.show()
        }
        findViewById<TextView>(R.id.btn_selector_2).setOnClickListener {
            loadingDialog.show()
        }
        findViewById<TextView>(R.id.btn_selector_3).setOnClickListener { }

        val text = tv_text.text
        TextSpanHelper.Builder(tv_text)
            .setColor(Color.BLUE)
            .setText(text.toString())
            .isGroup(false)
            .setPattern("[《](.*?)[》]")
            .setCallback {
                Toast.makeText(this, "click span text.", Toast.LENGTH_SHORT).show()
            }
            .create()
            .buildSpan()
    }


}
