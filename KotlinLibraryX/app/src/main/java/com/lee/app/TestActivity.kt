package com.lee.app

import android.content.Intent
import android.widget.Button
import android.widget.Toast
import com.lee.app.databinding.ActivityMainBinding
import com.lee.app.server.ApiServiceImpl
import com.lee.library.base.BaseActivity
import com.lee.library.dialog.ChoiceDialog
import com.lee.library.dialog.LoadingDialog
import com.lee.library.dialog.WarnDialog
import com.lee.library.extensions.binding
import com.lee.library.tools.StatusTools.setDarkStatusIcon
import com.lee.library.tools.StatusTools.statusBar
import com.lee.library.utils.DensityUtil
import com.lee.library.utils.LogUtil
import com.lee.library.widget.StatusLayout
import com.lee.library.widget.StatusLayout.Companion.STATUS_LOADING
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestActivity : BaseActivity() {

    private val binding by binding(ActivityMainBinding::inflate)

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
            onConfirm = { dismiss() }
        }
    }

    /**
     * 普通选择框 确认/取消操作
     */
    private val choiceDialog by lazy {
        ChoiceDialog(this).apply {
            setTitle("是否打开通讯设备?")
            onConfirm = {
                dismiss()
                Toast.makeText(context, "已开启通讯设备", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun bindView() {
        DensityUtil.setDensity(this)
        window.statusBar()
        setDarkStatusIcon()
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

    override fun bindData() {

    }

    private fun testDialog() {
        binding.btnShowLoading.setOnClickListener { loadingDialog.show() }
        binding.btnShowLoading.setButtonDisable(false)

        binding.btnShowWarn.setOnClickListener { warnDialog.show() }
        binding.btnShowChoice.setOnClickListener { choiceDialog.show() }
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
