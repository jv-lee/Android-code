package com.lee.api.fragment

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import com.lee.api.R
import com.lee.api.databinding.FragmentMainBinding
import com.lee.library.base.BaseVMFragment
import com.lee.library.extensions.toast

class StartFragment : BaseVMFragment<FragmentMainBinding, ViewModel>(R.layout.fragment_main) {

    private val forResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        toast(it?.data?.getStringExtra("value") ?: "")
    }

    override fun bindView() {
        binding.btnActivityResult.setOnClickListener {
            forResult.launch(Intent(requireContext(), ResultFragmentActivity::class.java))
        }
    }

    override fun bindData() {

    }


}