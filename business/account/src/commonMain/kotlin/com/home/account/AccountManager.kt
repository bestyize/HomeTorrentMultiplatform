package com.home.account

import com.home.account.model.User
import com.thewind.kmmkv.KmmKv
import com.thewind.network.HttpUtil
import com.thewind.utils.toObject
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


/**
 * @author: read
 * @date: 2023/8/20 上午2:13
 * @description:
 */
object AccountManager {

    private var _user: User? = null

    fun saveUser(user: User?) {
        user ?: return
        KmmKv.defaultKmmKv().encode(KEY_USER_INFO, Json.encodeToString(user))
        _user = user
        updateConfig()
    }

    fun getUser(): User? {
        if (_user == null) {
            runCatching {
                _user = KmmKv.defaultKmmKv().decode(KEY_USER_INFO).toObject(User::class)
            }
        }
        updateConfig()
        return _user
    }

    fun loginOut() {
        _user = null
        KmmKv.defaultKmmKv().remove(KEY_USER_INFO)
        updateConfig()
    }

    fun isLogin(): Boolean {
        return getUser() != null
    }


    private fun updateConfig() {
        HttpUtil.commonHeader["uid"] = _user?.uid?.toString() ?: ""
        HttpUtil.commonHeader["password"] = _user?.password ?: ""
    }
}

private const val KEY_USER_INFO = "key_user_info"