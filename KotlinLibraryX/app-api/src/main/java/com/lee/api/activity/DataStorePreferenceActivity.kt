package com.lee.api.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lee.api.R
import com.lee.api.tools.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DataStorePreferenceActivity : AppCompatActivity(R.layout.activity_data_store_preference) {

    companion object {
        const val TAG = "DataStore"
        val KEY = stringPreferencesKey("key")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            getData()
        }
    }

    private suspend fun getData() {
        dataStore.data
            .map { preferences ->
                preferences[KEY] ?: "null"
            }.collect {
                Log.i(TAG, "onCreate: ${Thread.currentThread()} -$it")
            }
    }

    private suspend fun putData() {
        dataStore.edit { settings ->
            settings[KEY] = "not-null"
        }
    }
}