package com.thewind.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

object HttpUtil {

    private val client by lazy { HttpClient(CIO) }

    suspend fun get(link: String?, headerMap: MutableMap<String, String> = mutableMapOf()): String {
        if (link == null || !link.startsWith("http")) {
            return ""
        }
        return try {
            val response: HttpResponse = client.get(link){
                headerMap.entries.forEach {
                    header(it.key, it.value)
                }
            }
            response.bodyAsText()
        } catch (e:Exception){
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
    fun post(
        link: String?, params: String?, headerMap: MutableMap<String, String> = mutableMapOf()
    ): String? {
        if (link == null || !link.startsWith("http")) {
            return ""
        }
        var response: String? = null

        return response
    }


}


private const val TAG = "HttpUtil"