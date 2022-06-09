package com.lee.library.net.adapter

import com.google.gson.*
import java.lang.reflect.Type

/**
 * json解析转换适配器 long类型
 * @author jv.lee
 * @date 2020/3/20
 */
class LongDefaultAdapter : JsonSerializer<Long>, JsonDeserializer<Long> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Long {
        json ?: return 0L
        return try {
            //定义为long类型,如果后台返回""或者null,则返回0
            if (json.asString == "" || json.asString == "null") {
                0L
            } else {
                json.asLong
            }
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