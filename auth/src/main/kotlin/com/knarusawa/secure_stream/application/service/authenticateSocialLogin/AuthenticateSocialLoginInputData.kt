package com.knarusawa.secure_stream.application.service.authenticateSocialLogin

data class AuthenticateSocialLoginInputData(
    val provider: String,
    val code: String,
    val state: String,
)