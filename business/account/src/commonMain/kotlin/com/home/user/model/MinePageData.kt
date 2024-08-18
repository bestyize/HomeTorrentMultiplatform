package com.home.user.model

import com.home.account.AccountManager
import com.home.account.model.User

data class MinePageData(
    val showLogin: Boolean = !AccountManager.isLogin(),
    val showLogout: Boolean = false,
    val user: User? = null,
)
