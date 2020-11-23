package com.lee.api.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.lee.api.R
import com.lee.api.proto.SettingsSerializer
import com.proto.model.SettingsProto
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DataStoreProtoActivity : AppCompatActivity(R.layout.activity_data_store_preference) {

    companion object {
        const val TAG = "DataStore"
    }

    private val dataStore: DataStore<SettingsProto.Settings> by lazy {
        createDataStore(
            fileName = "settings.pb",
            serializer = SettingsSerializer
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch {
            updateData()
            getData().collect {
                Log.i(TAG, "onCreate: exampleCounter:${it.exampleCounter}")
            }
        }

    }

    private fun getData(): Flow<SettingsProto.Settings> {
        return dataStore.data
//        dataStore.data
//            .map {
//                it.exampleCounter
//            }.collect {
//                Log.i(TAG, "getData: exampleCounter:$it")
//            }
    }

    private suspend fun updateData() {
        dataStore.updateData {
            it.toBuilder()
                .setExampleCounter(it.exampleCounter + 1)
                .build()
        }
    }

}