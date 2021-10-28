package com.lee.pdfjs

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.ProgressBar
import androidx.core.content.FileProvider
import com.lee.library.widget.AppWebView
import java.io.File

class MainActivity : AppCompatActivity() {

    private val web by lazy { findViewById<AppWebView>(R.id.web) }
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
//        web.loadUrl("file:///android_asset/viewer.html?" + "file:///android_asset/book.pdf")
        val file = File(filesDir.absolutePath + File.separator + "book.pdf")
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
        } else {
            Uri.fromFile(file)
        }
        web.loadUrl("file:///android_asset/viewer.html?$uri")
    }

    @JavascriptInterface
    fun end() {
        runOnUiThread { progress.visibility = View.GONE }
    }
}
