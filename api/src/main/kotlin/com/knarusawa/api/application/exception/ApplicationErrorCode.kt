package com.knarusawa.api.application.exception

import org.openapitools.model.ErrorError

enum class ApplicationErrorCode(
    val code: ErrorError.Code,
    val message: String
) {
    USER_NOT_FOUND(
        code = ErrorError.Code.ID400_E001,
        message = "対象のユーザーが見つかりませんでした"
    ),
}