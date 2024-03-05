package com.knarusawa.api.application.exception

data class UserNotFoundException(
    override val message: String = "対象のユーザーが見つかりませんでした",
    override val cause: Throwable? = null,
    override val code: ApplicationErrorCode = ApplicationErrorCode.USER_NOT_FOUND,
) : ApplicationException(message, cause, code)