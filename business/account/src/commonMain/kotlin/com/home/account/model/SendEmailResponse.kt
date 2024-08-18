package com.home.account.model

import kotlinx.serialization.Serializable


@Serializable
data class SendEmailResponse(val code: Int, val message: String)
