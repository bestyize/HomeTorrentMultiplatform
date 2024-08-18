package com.home.account

import com.home.account.model.User
import com.thewind.kmmkv.KmmKv
import com.thewind.utils.toJson
import com.thewind.utils.toObject


/**
 * @author: read
 * @date: 2023/8/20 上午2:13
 * @description:
 */
object AccountManager {

    private var _user: User? = null

    fun saveUser(user: User?) {
        user ?: return
        KmmKv.defaultKmmKv().encode(KEY_USER_INFO, user.toJson())
        _user = user
    }

    fun getUser(): User? {
        if (_user == null) {
            runCatching {
                _user = KmmKv.defaultKmmKv()
                    .decode(KEY_USER_INFO).toObject(User::class)
            }
        }
        return _user
    }

    fun loginOut() {
        _user = null
        KmmKv.defaultKmmKv().remove(KEY_USER_INFO)
    }

    fun isLogin(): Boolean {
        return getUser() != null
    }
}

private const val KEY_USER_INFO = "key_user_info"