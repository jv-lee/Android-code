package com.lee.library.net.adapter

import com.google.gson.*
import java.lang.reflect.Type

/**
 * @author jv.lee
 * @date 2020/3/20
 * @description
 */
class LongDefaultAdapter : JsonSerializer<Long>, JsonDeserializer<Long> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Long {
        try {
            if (json!!.asString == "" || json.asString == "null") { //定义为int类型,如果后台返回""或者null,则返回0
                return 0L
            }
        } catch (ignore: Exception) {
        }
        return try {
            json!!.asLong
        } catch (e: NumberFormatException) {
            throw JsonSyntaxException(e)
        }
    }

    override fun serialize(
        src: Long?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src)
    }
}