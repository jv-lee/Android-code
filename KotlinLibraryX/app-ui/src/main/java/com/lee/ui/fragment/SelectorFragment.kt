package com.lee.ui.fragment

import android.graphics.Color
import android.text.Html
import android.widget.Toast
import com.lee.library.base.BaseFragment
import com.lee.library.dialog.LoadingDialog
import com.lee.library.extensions.binding
import com.lee.library.tools.TextSpanHelper
import com.lee.library.widget.SnackBarEx
import com.lee.ui.R
import com.lee.ui.databinding.FragmentSelectorBinding

/**
 * 自定义文本选择器示范
 * @author jv.lee
 * @date 2021/1/12
 */
class SelectorFragment : BaseFragment(R.layout.fragment_selector) {

    private val binding by binding(FragmentSelectorBinding::bind)

    private val loadingDialog by lazy { LoadingDialog(requireActivity()) }

    private val snackBar by lazy {
        SnackBarEx.Builder(binding.linear)
            .setDuration(5000)
            .setActionText("action")
            .setMessage("message")
            .setOnClickListener { }
            .build()
    }

    override fun bindView() {
        binding.btnSelector1.setOnClickListener {
            snackBar.show()
        }
        binding.btnSelector2.setOnClickListener {
            loadingDialog.show()
            it.postDelayed({ loadingDialog.dismiss() }, 3000)
        }
        binding.btnSelector3.setOnClickListener { }
        binding.shadowLayout.setOnClickListener { }
    }

    override fun bindData() {
        val text = binding.tvText.text
        TextSpanHelper.Builder(binding.tvText)
            .setColor(Color.BLUE)
            .setText(text.toString())
            .isGroup(false)
            .setPattern("[《](.*?)[》]")
            .setCallback {
                Toast.makeText(requireActivity(), "click span text.", Toast.LENGTH_SHORT).show()
            }
            .create()
            .buildSpan()

        setHtmlText()
    }

    private fun setHtmlText() {
        val html =
            "<font color=\"#000000\">本次呼叫被禁止 </font><font color=\"#ff0000\">(存在风险呼叫行为)</font><b><font color=\"#000000\">。2月28日前</font></b><font color=\"#000000\">仅支持使用小度语音、视频通话呼叫家庭成员及互为通讯录联系人的用户。</font>"
        binding.tvHtml.text = Html.fromHtml(html)
    }
}