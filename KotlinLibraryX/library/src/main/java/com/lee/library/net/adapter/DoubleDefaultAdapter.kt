package com.lee.library.net.adapter

import com.google.gson.*
import java.lang.reflect.Type

/**
 *
 * @author jv.lee
 * @date 2020/3/20
 *
 */
class DoubleDefaultAdapter : JsonSerializer<Double>, JsonDeserializer<Double> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Double {
        try {
            if (json!!.asString == "" || json.asString == "null") { //定义为int类型,如果后台返回""或者null,则返回0
                return 0.0
            }
        } catch (ignore: Exception) {
        }
        return try {
            json!!.asDouble
        } catch (e: NumberFormatException) {
            throw JsonSyntaxException(e)
        }
    }

    override fun serialize(
        src: Double?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src)
    }
}