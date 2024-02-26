package com.knarusawa.secure_stream.application.service.requestWebauthnLogin

import com.knarusawa.common.domain.flow.FlowId
import com.webauthn4j.data.PublicKeyCredentialRequestOptions

data class RequestWebauthnLoginOutputData(
        val flowId: FlowId,
        val options: PublicKeyCredentialRequestOptions
)
