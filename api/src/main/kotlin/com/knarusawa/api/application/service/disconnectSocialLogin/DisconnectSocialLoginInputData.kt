package com.knarusawa.api.application.service.disconnectSocialLogin

data class DisconnectSocialLoginInputData(
    val userId: String,
    val provider: String
)