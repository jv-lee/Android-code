/*
 * DataStorePreferences 方式存储工具
 * @author jv.lee
 * @date 2022/6/22
 */
package com.lee.api.tools

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")