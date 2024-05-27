package com.lee.api.fragment

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import com.lee.api.databinding.FragmentMainBinding
import com.lee.library.base.BaseBindingFragment
import com.lee.library.extensions.toast

class StartFragment : BaseBindingFragment<FragmentMainBinding>() {

    private val forResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        toast(it?.data?.getStringExtra("value") ?: "")
    }

    override fun bindView() {
        mBinding.btnActivityResult.setOnClickListener {
            forResult.launch(Intent(requireContext(), ResultFragmentActivity::class.java))
        }
    }

    override fun bindData() {
    }
}