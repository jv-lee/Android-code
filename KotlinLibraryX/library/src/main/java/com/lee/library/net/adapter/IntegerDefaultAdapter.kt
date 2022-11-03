package com.lee.library.net.adapter

import com.google.gson.*
import java.lang.reflect.Type

/**
 * json解析转换适配器 int类型
 * @author jv.lee
 * @date 2020/3/20
 */
class IntegerDefaultAdapter : JsonSerializer<Int>, JsonDeserializer<Int> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Int {
        json ?: return 0
        return try {
            // 定义为int类型,如果后台返回""或者null,则返回0
            if (json.asString == "" || json.asString == "null") {
                0
            } else {
                json.asInt
            }
        } catch (e: NumberFormatException) {
            throw JsonSyntaxException(e)
        }
    }

    override fun serialize(
        src: Int?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src)
    }
}