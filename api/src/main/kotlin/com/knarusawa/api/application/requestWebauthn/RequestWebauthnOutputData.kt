package com.knarusawa.api.application.requestWebauthn

import com.knarusawa.common.domain.flow.FlowId
import com.webauthn4j.data.PublicKeyCredentialCreationOptions


class RequestWebauthnOutputData(
        val flowId: FlowId,
        val options: PublicKeyCredentialCreationOptions
) {
    fun from(
            flowId: FlowId,
            options: PublicKeyCredentialCreationOptions
    ): RequestWebauthnOutputData {
        return RequestWebauthnOutputData(
                flowId = flowId,
                options = options
        )
    }
}