package com.knarusawa.api.application.disconnectSocialLogin

data class DisconnectSocialLoginInputData(
    val userId: String,
    val provider: String
)