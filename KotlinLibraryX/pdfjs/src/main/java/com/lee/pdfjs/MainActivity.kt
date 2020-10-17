package com.lee.pdfjs

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.ProgressBar
import android.widget.Toast
import com.lee.library.widget.WebViewEx
import java.io.File

class MainActivity : AppCompatActivity() {

    private val web by lazy { findViewById<WebViewEx>(R.id.web) }
    private val progress by lazy { findViewById<ProgressBar>(R.id.progress) }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        web.settings.run {
            javaScriptEnabled = true
            allowFileAccess = true
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true

            setSupportZoom(true)
            builtInZoomControls = true
            useWideViewPort = true
        }
        web.addJavascriptInterface(this, "control")

        //"file:///android_asset/viewer.html?${Uri.fromFile(File(""))}"
        web.loadUrl("file:///android_asset/viewer.html?" + "file:///android_asset/book.pdf")
    }

    @JavascriptInterface
    fun end() {
        runOnUiThread { progress.visibility = View.GONE }
    }
}
