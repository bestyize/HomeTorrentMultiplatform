package com.home.user.model

data class LoginPageData(
    val pageStage: LoginPageStage = LoginPageStage.LOGIN,
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val verifyCode: String = ""
)
