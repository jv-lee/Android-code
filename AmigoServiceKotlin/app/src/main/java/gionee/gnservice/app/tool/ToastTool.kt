package gionee.gnservice.app.tool

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import gionee.gnservice.app.R

/**
 * @author jv.lee
 * @date 2019/8/30.
 * @description
 */
class ToastTool{

    companion object {

        fun show(context: Context, text: String) {
            val layoutParams =
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            val toastView = View.inflate(context, R.layout.layout_reward_toast, null)
            val linearLayout = toastView.findViewById<FrameLayout>(R.id.frame_container)
            val tvText = toastView.findViewById<TextView>(R.id.tv_text)
            linearLayout.layoutParams = layoutParams
            tvText.text = text

            val displayToast = Toast(context)
            displayToast.view = toastView
            displayToast.duration = Toast.LENGTH_SHORT
            displayToast.setGravity(Gravity.CENTER or Gravity.BOTTOM, 0, 36)
            displayToast.show()
        }

    }

}