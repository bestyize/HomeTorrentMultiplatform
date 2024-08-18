package com.home.account.model

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val uid: Long = -1,
    val userName: String = "",
    val password: String = "",
    val email: String = "",
    val registerTime: Long = -1,
    val lastLoginTime: Long = -1,
    val icon: String = "",
    val level: Int = 0,
)