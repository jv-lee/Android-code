package com.lee.library.net.adapter

import com.google.gson.*
import java.lang.reflect.Type

/**
 * json解析转换适配器 double类型
 * @author jv.lee
 * @date 2020/3/20
 */
class DoubleDefaultAdapter : JsonSerializer<Double>, JsonDeserializer<Double> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Double {
        json ?: return 0.0
        return try {
            // 定义为double类型,如果后台返回""或者null,则返回0
            if (json.asString == "" || json.asString == "null") {
                0.0
            } else {
                json.asDouble
            }
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