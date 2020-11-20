package com.lee.api.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.lee.api.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DataStorePreferenceActivity : AppCompatActivity(R.layout.activity_data_store_preference) {

    companion object {
        const val TAG = "DataStore"
    }

    private val dataStore by lazy { createDataStore(name = "settings") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch {
            getData()
        }

    }

    private suspend fun getData() {
        dataStore.data
            .map {
                it[preferencesKey<String>("test_key")] ?: "null"
            }.collect {
                Log.i(TAG, "onCreate: ${Thread.currentThread()} -$it")
            }
    }

    private suspend fun putData() {
        dataStore.edit {
            it[preferencesKey<String>("test_key")] = "not-null"
        }
    }

}