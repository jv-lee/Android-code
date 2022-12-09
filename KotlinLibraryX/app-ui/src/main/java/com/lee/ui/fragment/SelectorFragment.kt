package com.lee.ui.fragment

import android.graphics.Color
import android.widget.Toast
import com.lee.library.base.BaseFragment
import com.lee.library.dialog.LoadingDialog
import com.lee.library.extensions.binding
import com.lee.library.tools.TextSpanHelper
import com.lee.library.widget.SnackBarEx
import com.lee.ui.R
import com.lee.ui.databinding.FragmentSelectorBinding

/**
 *
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
        binding.btnSelector3.setOnClickListener {
        }
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
    }
}