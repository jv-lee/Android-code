package com.lee.api.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lee.api.R
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

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

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