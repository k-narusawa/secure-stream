package com.knarusawa.api.application.connectSocialLogin

data class ConnectSocialLoginInputData(
    val userId: String,
    val provider: String,
    val code: String,
    val state: String,
)