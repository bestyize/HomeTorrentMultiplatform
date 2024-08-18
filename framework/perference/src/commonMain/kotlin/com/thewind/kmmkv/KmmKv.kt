package com.thewind.kmmkv

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.thewind.perference.platform.getDefaultDatastore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class KmmKv {

    private val kmmKvScope = CoroutineScope(Dispatchers.IO)

    fun encode(key: String, value: String) {
        kmmKvScope.launch {
            getDefaultDatastore().edit {
                it[stringPreferencesKey(key)] = value
            }
        }
    }

    fun decode(key: String, defaultValue: String? = null): String? {
        return runBlocking {
            return@runBlocking getDefaultDatastore().data.first()[stringPreferencesKey(key)] ?: defaultValue
        }
    }

    fun <T : Number> encode(key: String, value: T) {
        kmmKvScope.launch {
            getDefaultDatastore().edit {
                it[stringPreferencesKey(key)] = value.toString()
            }
        }
    }

    fun <T : Number> decode(key: String, defaultValue: T): T {
        return runBlocking {
            val numStr = getDefaultDatastore().data.first()[stringPreferencesKey(key)] ?: defaultValue.toString()
            val num = try {
                when (defaultValue) {
                    is Int -> numStr.toInt()
                    is Long -> numStr.toLong()
                    is Float -> numStr.toFloat()
                    is Double -> numStr.toDouble()
                    is Short -> numStr.toShort()
                    is Byte -> numStr.toByte()
                    else -> defaultValue
                }
            } catch (_: Exception) {
                defaultValue
            }
            return@runBlocking num as T
        }
    }

    companion object {
        private val mmkv by lazy { KmmKv() }
        fun defaultKmmKv() = mmkv
    }
}