package com.knarusawa.api.application.deleteWebauthn

import com.knarusawa.common.domain.user.UserId

data class DeleteWebauthnInputData(
    val userId: UserId
)