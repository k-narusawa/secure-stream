package com.knarusawa.api.application.exception

import org.openapitools.model.Model400ErrorError

enum class ApplicationErrorCode(
    val code: Model400ErrorError.Code,
    val message: String
) {
    USER_NOT_FOUND(
        code = Model400ErrorError.Code.ID400_E001,
        message = "対象のユーザーが見つかりませんでした"
    ),
}