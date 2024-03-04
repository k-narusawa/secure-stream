package com.knarusawa.api.application.service.connectSocialLogin

data class ConnectSocialLoginInputData(
    val userId: String,
    val provider: String,
    val code: String,
    val state: String,
)