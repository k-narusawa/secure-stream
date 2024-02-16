package com.knarusawa.secure_stream.application.service.loginFailure

import java.time.LocalDateTime

data class LoginFailureInputData(
        val username: String?,
        val remoteAddr: String,
        val userAgent: String,
        val time: LocalDateTime,
)