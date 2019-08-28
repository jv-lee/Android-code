package gionee.gnservice.app.view.fragment


import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v4.app.Fragment

import com.lee.library.base.BaseFragment
import com.lee.library.ioc.annotation.ContentView
import gionee.gnservice.app.R
import gionee.gnservice.app.databinding.FragmentWalletBinding

/**
 * A simple [Fragment] subclass.
 *
 */
class WalletFragment : BaseFragment<FragmentWalletBinding, ViewModel>(R.layout.fragment_wallet, null) {

    override fun bindData(savedInstanceState: Bundle?) {
        binding.web.loadUrl("file:///android_asset/html/wallet/index.html")
    }

    override fun bindView() {
    }


}
