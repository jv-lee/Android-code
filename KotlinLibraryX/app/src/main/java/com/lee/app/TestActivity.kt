package com.lee.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lee.app.server.ApiService
import com.lee.app.server.ApiServiceImpl
import com.lee.library.net.HttpManager
import com.lee.library.net.request.IRequest
import com.lee.library.net.request.Request
import com.lee.library.utils.LogUtil
import com.lee.library.widget.StatusLayout
import com.lee.library.widget.StatusType.STATUS_LOADING
import com.lee.library.widget.dialog.ChoiceDialog
import com.lee.library.widget.dialog.LoadingDialog
import com.lee.library.widget.dialog.WarnDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
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
    private val warnDialog: WarnDialog by lazy {
        WarnDialog.build(this, "您输入的密码有误，请重新输入")
            .apply {
                setConfirmListener {
                    dismiss()
                }
            }
    }

    /**
     * 普通选择框 确认/取消操作
     */
    private val choiceDialog: ChoiceDialog by lazy {
        ChoiceDialog.build(this, "是否打开通讯设备？")
            .apply {
                setConfirmListener {
                    dismiss()
                    Toast.makeText(context, "已开启通讯设备", Toast.LENGTH_SHORT).show()
                }
                setCancelListener {
                    dismiss()
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testDialog()
        testNetwork()
        findViewById<StatusLayout>(R.id.status).setStatus(STATUS_LOADING)
    }

    private fun testDialog() {
        btn_show_loading.setOnClickListener { loadingDialog.show() }
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
