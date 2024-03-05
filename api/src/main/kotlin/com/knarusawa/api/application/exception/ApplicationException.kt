package com.knarusawa.api.application.exception

open class ApplicationException(
    override val message: String,
    override val cause: Throwable?,
    open val code: ApplicationErrorCode,
) : RuntimeException(message, cause) 