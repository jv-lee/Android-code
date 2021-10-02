package com.lee.api.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lee.api.R
import com.lee.api.proto.SettingsSerializer.settingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DataStoreProtoActivity : AppCompatActivity(R.layout.activity_data_store_preference) {

    companion object {
        const val TAG = "DataStore"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            updateData()
            getData()
        }
        blocking()
    }

    /**
     * 异步方式修改方式
     */
    private suspend fun updateData() {
        settingsDataStore.updateData { settings ->
            settings.toBuilder()
                .setExampleCounter(1)
                .build()
        }
    }

    /**
     * 异步方式获取方式
     */
    private suspend fun getData() {
        settingsDataStore.data.collect {
            Log.i(TAG, "exampleCounter:${it.exampleCounter}")
        }
    }

    /**
     * 同步获取方式
     */
    private fun blocking() {
        val settings = runBlocking { settingsDataStore.data.first() }
        Log.i(TAG, "blocking:${settings.exampleCounter}")
        lifecycleScope.launch {
            Log.i(
                TAG,
                "blocking: ${settingsDataStore.data.first().exampleCounter}"
            )
        }
    }

}