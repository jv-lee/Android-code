package com.lee.api.proto

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.proto.model.Settings
import java.io.InputStream
import java.io.OutputStream


/**
 *
 * @author jv.lee
 * @date 2020/11/20

 */
object SettingsSerializer : Serializer<Settings> {
    override val defaultValue: Settings = Settings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Settings {
        try {
            return Settings.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: Settings, output: OutputStream) = t.writeTo(output)

    val Context.settingsDataStore: DataStore<Settings> by dataStore(
        fileName = "settings.pb",
        serializer = SettingsSerializer
    )
}