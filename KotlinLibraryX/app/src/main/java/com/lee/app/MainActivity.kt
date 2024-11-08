package com.lee.app

import android.content.Intent
import android.widget.Toast
import com.lee.app.databinding.ActivityMainBinding
import com.lee.app.server.ApiServiceImpl
import com.lee.library.base.BaseActivity
import com.lee.library.connect.NetworkConnectCallback
import com.lee.library.connect.NetworkConnectManager
import com.lee.library.connect.NetworkType
import com.lee.library.dialog.ChoiceDialog
import com.lee.library.dialog.LoadingDialog
import com.lee.library.dialog.WarnDialog
import com.lee.library.extensions.binding
import com.lee.library.extensions.toast
import com.lee.library.tools.SystemBarTools.setDarkStatusIcon
import com.lee.library.utils.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : BaseActivity(), NetworkConnectCallback {

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
        window.setDarkStatusIcon()

        binding.btnChat.setOnClickListener {
            startActivity(Intent(this, ChatListActivity::class.java))
        }
        binding.btnForm.setOnClickListener {
            startActivity(Intent(this, FormTableActivity::class.java))
        }
    }

    override fun bindData() {
        testDialog()
        NetworkConnectManager.instance.bindCallback(this, isNotification = true)
    }

    override fun onDestroy() {
        super.onDestroy()
        NetworkConnectManager.instance.unbindCallback(this)
    }

    private fun testDialog() {
        binding.btnShowLoading.setOnClickListener { loadingDialog.show() }
//        binding.btnShowLoading.setButtonDisable(false)

        binding.btnShowWarn.setOnClickListener { warnDialog.show() }
        binding.btnShowChoice.setOnClickListener { choiceDialog.show() }
    }

    private fun testNetwork() {
        CoroutineScope(Dispatchers.Main).launch {
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
                this@MainActivity,
                "data1 = ${data1.data.size} - data2 = ${data2.data.size}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onConnect() {
        toast("网络已连接")
    }

    override fun unConnect() {
        toast("网络已断开")
    }

    override fun networkTypeChange(type: NetworkType) {
        when (type) {
            NetworkType.Wifi -> toast("当前网络类型为wifi")
            NetworkType.Network -> toast("当前网络类型为network")
            NetworkType.Other -> toast("当前网络类型为other")
            else -> {}
        }
    }
}
