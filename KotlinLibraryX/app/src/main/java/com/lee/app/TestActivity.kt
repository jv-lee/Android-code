package com.lee.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lee.app.server.ApiServiceImpl
import com.lee.library.dialog.ChoiceDialog
import com.lee.library.dialog.LoadingDialog
import com.lee.library.dialog.WarnDialog
import com.lee.library.dialog.core.ConfirmListener
import com.lee.library.utils.DensityUtil
import com.lee.library.utils.LogUtil
import com.lee.library.tools.StatusTools
import com.lee.library.widget.StatusLayout
import com.lee.library.widget.StatusLayout.STATUS_LOADING
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestActivity : AppCompatActivity() {

    /**
     * loading提示框
     */
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this).apply {
            setCancelable(true)
        }

    }

    /**
     * 普通提示框 单确认按钮
     */
    private val warnDialog by lazy {
        WarnDialog(this).apply {
            setTitle("您输入的密码有误，请重新输入")
            confirmListener = ConfirmListener { dismiss() }
        }
    }

    /**
     * 普通选择框 确认/取消操作
     */
    private val choiceDialog by lazy {
        ChoiceDialog(this).apply {
            setTitle("是否打开通讯设备?")
            confirmListener = ConfirmListener {
                dismiss()
                Toast.makeText(context, "已开启通讯设备", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DensityUtil.setDensity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusTools.statusBar(window, false)
        StatusTools.setDarkStatusIcon(this)
        testDialog()
//        testNetwork()
        findViewById<StatusLayout>(R.id.status).setStatus(STATUS_LOADING)
        findViewById<Button>(R.id.btn_chat).setOnClickListener {
            startActivity(Intent(this, ChatListActivity::class.java))
        }
        findViewById<Button>(R.id.btn_form).setOnClickListener {
            startActivity(Intent(this, FormTableActivity::class.java))
        }
    }

    private fun testDialog() {
        btn_show_loading.setOnClickListener { loadingDialog.show() }
        btn_show_loading.setButtonDisable(false)

        btn_show_warn.setOnClickListener { warnDialog.show() }
        btn_show_choice.setOnClickListener { choiceDialog.show() }
    }

    private fun testNetwork() {
        GlobalScope.launch(Dispatchers.Main) {
            val data1 = withContext(Dispatchers.IO) {
                ApiServiceImpl.get()
                    .api.getTabAsync("http://www.dell-lee.com/react/api/header.json").await()
            }
            LogUtil.i("time:${System.currentTimeMillis()}")

            val data2 = withContext(Dispatchers.IO) {
                ApiServiceImpl.get()
                    .api.getTabAsync("http://www.dell-lee.com/react/api/header.json").await()
            }
            LogUtil.i("time:${System.currentTimeMillis()}")

            Toast.makeText(
                this@TestActivity,
                "data1 = ${data1.data.size} - data2 = ${data2.data.size}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

}
