package com.knarusawa.secure_stream.application.service.loginComplete

import java.time.LocalDateTime


data class LoginCompleteInputData(
        val username: String,
        val remoteAddr: String,
        val userAgent: String,
        val time: LocalDateTime,
)