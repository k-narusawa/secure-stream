package com.knarusawa.api.application.exception

data class UserNotFoundException(
    override val message: String = "対象のユーザーが見つかりませんでした",
    override val cause: Throwable? = null
) : RuntimeException(message, cause)