package com.knarusawa.api.application.requestWebauthn

import com.knarusawa.common.domain.user.UserId

data class RequestWebauthnInputData(
        val userId: UserId,
        val authenticatorAttachment: AuthenticatorAttachment
) {
    enum class AuthenticatorAttachment {
        CROSS_PLATFORM,
        PLATFORM
    }
}