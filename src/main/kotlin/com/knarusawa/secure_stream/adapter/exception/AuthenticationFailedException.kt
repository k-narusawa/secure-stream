package com.knarusawa.secure_stream.adapter.exception

import org.springframework.security.core.AuthenticationException

class AuthenticationFailedException(
        val username: String?,
        override val message: String = "認証に失敗しました",
        override val cause: Throwable? = null
) : AuthenticationException(message, cause)