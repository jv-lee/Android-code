package com.lee.api.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lee.api.R
import com.lee.api.proto.SettingsSerializer.settingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DataStoreProtoActivity : AppCompatActivity(R.layout.activity_data_store_preference) {

    companion object {
        const val TAG = "DataStore"
    }

    private val settings = runBlocking { settingsDataStore.data.first() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            updateData()
            getData()
        }
    }

    private suspend fun updateData() {
        settingsDataStore.updateData { settings ->
            settings.toBuilder()
                .setExampleCounter(settings.exampleCounter + 1)
                .build()
        }
    }

    private suspend fun getData() {
        settingsDataStore.data.map { settings ->
            Log.i(TAG, "exampleCounter:${settings.exampleCounter}")
        }
    }

    private fun blocking() {
        Log.i(TAG, "exampleCounter:${settings.exampleCounter}")
        lifecycleScope.launch {
            Log.i(TAG, "blocking: ${settingsDataStore.data.first().exampleCounter}")
        }
    }

}