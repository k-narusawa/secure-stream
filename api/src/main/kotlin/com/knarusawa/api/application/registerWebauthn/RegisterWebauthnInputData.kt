package com.knarusawa.api.application.registerWebauthn

import com.knarusawa.common.domain.flow.FlowId
import com.knarusawa.common.domain.user.UserId

data class RegisterWebauthnInputData(
        val userId: UserId,
        val flowId: FlowId,
        val id: String,
        val rawId: String,
        val type: String,
        val attestationObject: String,
        val clientDataJSON: String,
)