package gionee.gnservice.app.view.fragment


import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.webkit.JavascriptInterface
import com.lee.library.base.BaseDialogFragment
import com.lee.library.widget.WebViewEx
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.databinding.FragmentDialogActiveBinding
import gionee.gnservice.app.view.activity.MainActivity
import gionee.gnservice.app.view.native.JSInterface

/**
 * A simple [Fragment] subclass.
 *
 */
class DialogActiveFragment : BaseDialogFragment<FragmentDialogActiveBinding, ViewModel>(R.layout.fragment_dialog_active, null) {

    companion object {
        fun get(url: String): DialogActiveFragment {
            val fragment = DialogActiveFragment()
            val bundle = Bundle()
            bundle.putString(Constants.URL, url)
            fragment.arguments = bundle
            return fragment
        }
    }

    @SuppressLint("JavascriptInterface")
    override fun bindData(savedInstanceState: Bundle?) {
        binding.web.addJavascriptInterface(this, "simple")
        binding.web.addJavascriptInterface(JSInterface(context!!), "client")
        binding.web.addWebStatusListenerAdapter(object : WebViewEx.WebStatusListenerAdapter() {
            override fun callSuccess() {
                super.callSuccess()
                binding.ivClose.visibility = View.VISIBLE
                binding.web.visibility = View.VISIBLE
                binding.progress.visibility = View.GONE
            }
        })
        binding.web.loadUrl(arguments!![Constants.URL] as String?)
    }

    override fun bindView() {
        binding.ivClose.setOnClickListener { dismiss() }
    }

    @JavascriptInterface
    fun startPage(code: Int, arg: String) {
        if (activity is MainActivity) {
            activity?.runOnUiThread { (activity as? MainActivity)?.startTabMode(code) }
        }
    }

}
