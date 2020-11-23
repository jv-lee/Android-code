package com.lee.api.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.proto.model.SettingsProto
import java.io.InputStream
import java.io.OutputStream


/**
 * @author jv.lee
 * @date 2020/11/20
 * @description
 */
object SettingsSerializer : Serializer<SettingsProto.Settings> {
    override val defaultValue: SettingsProto.Settings
        get() = SettingsProto.Settings.getDefaultInstance()

    override fun readFrom(input: InputStream): SettingsProto.Settings {
        try {
            return SettingsProto.Settings.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override fun writeTo(t: SettingsProto.Settings, output: OutputStream) {
        return t.writeTo(output)
    }

}