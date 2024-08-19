package com.thewind.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

object HttpUtil {

    val commonHeader = mutableMapOf<String, String>()

    private val client by lazy {
        HttpClient(CIO)
    }

    suspend fun get(link: String?, headerMap: MutableMap<String, String> = mutableMapOf()): String {
        if (link == null || !link.startsWith("http")) {
            return ""
        }
        return try {
            headerMap.putAll(commonHeader)
            val response: HttpResponse = client.get(link) {
                headerMap.entries.forEach {
                    header(it.key, it.value)
                }
            }
            response.bodyAsText()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 做POST请求
     *
     * @param link      请求地址
     * @param params    请求体，类似于keyword=十年&num=100这样的格式
     * @param headerMap 请求头
     * @return 请求结果
     */
    suspend fun post(
        link: String?, params: String?, headerMap: MutableMap<String, String> = mutableMapOf()
    ): String? {
        if (link == null || !link.startsWith("http")) {
            return ""
        }
        return try {
            val response: HttpResponse = client.post(link) {
                headerMap.entries.forEach {
                    header(it.key, it.value)
                }
                setBody(params)
            }
            response.bodyAsText()
        } catch (e: Exception) {
            null
        }
    }


}


private const val TAG = "HttpUtil"