package com.home.account.model

import kotlinx.serialization.Serializable


@Serializable
data class LoginResponse(val code: Int = 0, val message: String = "", var user: User? = null)