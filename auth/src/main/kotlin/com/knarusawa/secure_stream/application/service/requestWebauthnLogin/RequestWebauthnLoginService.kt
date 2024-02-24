package com.knarusawa.secure_stream.application.service.requestWebauthnLogin

import com.knarusawa.common.domain.flow.Flow
import com.knarusawa.common.domain.flow.FlowRepository
import com.webauthn4j.data.PublicKeyCredentialDescriptor
import com.webauthn4j.data.PublicKeyCredentialRequestOptions
import com.webauthn4j.data.PublicKeyCredentialRpEntity
import com.webauthn4j.data.UserVerificationRequirement
import com.webauthn4j.data.client.challenge.DefaultChallenge
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RequestWebauthnLoginService(
        private val flowRepository: FlowRepository
) {
    companion object {
        private val RP = PublicKeyCredentialRpEntity("localhost", "localhost")
    }

    @Transactional
    fun exec(): RequestWebauthnLoginOutputData {
        val challenge = DefaultChallenge()
        val flow = Flow.of(userId = null, challenge = challenge)

        val allowCredentials = listOf<PublicKeyCredentialDescriptor>()

        val options = PublicKeyCredentialRequestOptions(
                challenge,
                60000,
                RP.id,
                allowCredentials,
                UserVerificationRequirement.DISCOURAGED,
                null
        )

        flowRepository.save(flow)

        return RequestWebauthnLoginOutputData(
                flowId = flow.flowId,
                options = options
        )
    }
}