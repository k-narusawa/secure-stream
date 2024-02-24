package com.knarusawa.secure_stream.application.exception

data class UserNotFoundException(
        override val message: String = "ユーザーの特定に失敗しました",
        override val cause: Throwable? = null
) : RuntimeException(message, cause)