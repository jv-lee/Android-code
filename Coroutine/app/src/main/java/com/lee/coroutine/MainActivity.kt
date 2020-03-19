package com.lee.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lee.coroutine.test.PlanBStart
import com.lee.coroutine.test.PlanCDispatcher

/**
 * @author jv.lee
 * @date 2020.03.19
 * @description
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        PlanBStart().test()

        PlanCDispatcher().test()
    }
}
