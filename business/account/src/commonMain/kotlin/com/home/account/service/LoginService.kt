package com.home.account.service

import com.home.account.model.LoginResponse
import com.home.account.model.RegisterResponse
import com.home.account.model.SendEmailResponse
import com.home.account.AccountManager
import com.thewind.network.HttpUtil.get
import com.thewind.network.appHost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/**
 * @author: read
 * @date: 2023/8/20 上午1:34
 * @description:
 */


object LoginService {
    suspend fun sendVerifyCode(email: String) = withContext(Dispatchers.IO) {
        runCatching {
            val resp = get("$appHost/user/api/send/verify/code?email=$email")
            if (resp.isBlank()) return@withContext SendEmailResponse(-1, "Network Error")
            val response: SendEmailResponse? = Json.decodeFromString(resp)
            response?.let {
                return@withContext it
            }
        }

        return@withContext SendEmailResponse(-1, "Network Error")
    }

    suspend fun register(userName: String, email: String, password: String, verifyCode: String) =
        withContext(Dispatchers.IO) {
            runCatching {
                val resp =
                    get("$appHost/user/api/register?userName=$userName&email=$email&password=$password&verifyCode=$verifyCode")
                val response: RegisterResponse? = Json.decodeFromString(resp)
                response?.let {
                    AccountManager.saveUser(it.user)
                    return@withContext it
                }
            }
            return@withContext RegisterResponse(-1, "Network Error")

        }


    suspend fun login(userName: String?, password: String, email: String?) = withContext(Dispatchers.IO) {
        runCatching {
            val resp = get("$appHost/user/api/login?userName=${userName ?: ""}&email=${email ?: ""}&password=$password")
            val response: LoginResponse? = Json.decodeFromString(resp)
            response?.let {
                AccountManager.saveUser(it.user)
                return@withContext it
            }
        }
        return@withContext LoginResponse(-1, "Network Error")
    }

    suspend fun modifyPassword(verifyCode: String, email: String, newPassword: String) = withContext(Dispatchers.IO) {
        runCatching {
            val resp =
                get("$appHost/user/api/modify/password?email=$email&verifyCode=$verifyCode&newPassword=$newPassword")
            val response: LoginResponse? = Json.decodeFromString(resp)
            response?.let {
                AccountManager.saveUser(it.user)
                return@withContext it
            }
        }
        return@withContext LoginResponse(-1, "Network Error")
    }


}
