package com.home.torrent.app

import com.home.account.AccountManager

class AppViewModel {

    fun init() {
        AccountManager.getUser()
    }


    companion object {
        val INSTANCE by lazy { AppViewModel() }
    }
}