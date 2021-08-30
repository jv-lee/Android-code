package com.lee.coroutine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lee.coroutine.test.PlanEModel
import kotlinx.coroutines.runBlocking

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

//        PlanCDispatcher().test(

//        PlanDFlow().test()

        val planEModel = PlanEModel()

        planEModel.simpleFun()

        runBlocking {
            planEModel.suspendFun()
        }

        planEModel.simple2Fun()
    }
}
