package com.lee.pdfjs

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.lee.library.widget.AppWebView
import java.io.File

/**
 * 在webView中使用JS-PDF库，打开pdf文件示例demo
 */
@SuppressLint("SetJavaScriptEnabled")
class MainActivity : AppCompatActivity() {

    private val web by lazy { findViewById<AppWebView>(R.id.web) }
    private val progress by lazy { findViewById<ProgressBar>(R.id.progress) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        web.settings.run {
            javaScriptEnabled = true
            allowFileAccess = true
            builtInZoomControls = true
            useWideViewPort = true
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true
            setSupportZoom(true)
        }
        web.addJavascriptInterface(this, "control")

        loadAssets()
    }

    private fun loadFile(path: String = filesDir.absolutePath + File.separator + "book.pdf") {
        val file = File(path)
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
        } else {
            Uri.fromFile(file)
        }
        web.loadUrl("file:///android_asset/viewer.html?$uri")
    }

    private fun loadAssets() {
        web.loadUrl("file:///android_asset/viewer.html?" + "file:///android_asset/book.pdf")
    }

    @JavascriptInterface
    fun end() {
        runOnUiThread { progress.visibility = View.GONE }
    }
}
