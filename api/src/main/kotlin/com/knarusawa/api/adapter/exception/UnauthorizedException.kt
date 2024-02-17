package com.knarusawa.api.adapter.exception

import org.springframework.security.core.AuthenticationException

data class UnauthorizedException(
        override val message: String = "認証に失敗しました",
        override val cause: Throwable? = null
) : AuthenticationException(message, cause)