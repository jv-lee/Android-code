package com.simple.vm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lee.library.extensions.binding
import com.simple.vm.constants.USER_ID_KEY
import com.simple.vm.databinding.ActivityMainBinding

/**
 *
 * @author jv.lee
 * @date 2021/8/19

 */
class MainActivity : AppCompatActivity() {

    private val binding by binding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.button.setOnClickListener {
            startActivity(
                Intent(this, ViewModelActivity::class.java).putExtra(
                    USER_ID_KEY,
                    "Activity Key Id"
                )
            )
        }
    }
}