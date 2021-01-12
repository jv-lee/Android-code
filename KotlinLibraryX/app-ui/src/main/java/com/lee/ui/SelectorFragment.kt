package com.lee.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.lee.library.dialog.LoadingDialog
import com.lee.library.utils.TextSpanHelper
import com.lee.library.widget.SnackBarEx
import com.lee.library.widget.nav.NumberDotView
import kotlinx.android.synthetic.main.fragment_selector.*

/**
 * @author jv.lee
 * @date 2021/1/12
 * @description
 */
class SelectorFragment : Fragment(R.layout.fragment_selector) {

    private val snackBar by lazy {
        SnackBarEx.Builder(linear)
            .setDuration(5000)
            .setActionText("action")
            .setMessage("message")
            .setOnClickListener { }
            .build()
    }

    private val loadingDialog by lazy { LoadingDialog(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.btn_selector_1).setOnClickListener {
            snackBar.show()
        }
        view.findViewById<TextView>(R.id.btn_selector_2).setOnClickListener {
            loadingDialog.show()
            it.postDelayed({ loadingDialog.dismiss() }, 3000)
        }
        view.findViewById<TextView>(R.id.btn_selector_3).setOnClickListener { }

        val text = tv_text.text
        TextSpanHelper.Builder(tv_text)
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