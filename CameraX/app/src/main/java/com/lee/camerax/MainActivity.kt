package com.lee.camerax

import com.lee.camerax.base.BaseActivity
import com.lee.camerax.databinding.ActivityMainBinding

/**
 * @author jv.lee
 * @date 2021/3/23
 * @description CameraX 示例
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun bindViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun bindView() {
        binding.tvText.text = "Update Text Content."
    }

}