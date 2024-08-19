package com.thewind.utils

import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass


/**
 * @author: read
 * @date: 2023/8/20 上午2:17
 * @description:
 */


fun Any?.toJson(): String {
    this ?: return "null"
    return Json.encodeToString(this)
}

inline fun <reified T : Any> String?.toJson(): String? {
    this ?: return null
    runCatching {
        return Json.decodeFromString(this) ?: return null
    }
    return null
}

inline fun <reified T : Any> String?.toObject(clazz: KClass<T>): T? {
    this ?: return null
    runCatching {
        return Json.decodeFromString(this) ?: return null
    }
    return null
}


val String.urlEncode: String
    get() = encodeURLPath()

