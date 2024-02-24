package com.knarusawa.api.application.requestWebauthn

import com.knarusawa.api.config.Environments
import com.knarusawa.common.domain.flow.Flow
import com.knarusawa.common.domain.flow.FlowRepository
import com.knarusawa.common.domain.user.UserRepository
import com.webauthn4j.data.*
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.util.Base64UrlUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class RequestWebauthnService(
        private val flowRepository: FlowRepository,
        private val userRepository: UserRepository,
        private val environments: Environments
) {
    @Transactional
    fun exec(inputData: RequestWebauthnInputData): RequestWebauthnOutputData {
        val challenge = DefaultChallenge()

        val pubKeyCredParams = listOf(
                PublicKeyCredentialParameters(
                        PublicKeyCredentialType.PUBLIC_KEY,
                        COSEAlgorithmIdentifier.ES256
                ),
                PublicKeyCredentialParameters(
                        PublicKeyCredentialType.PUBLIC_KEY,
                        COSEAlgorithmIdentifier.RS256
                ),
        )

        val username = userRepository.findByUserId(userId = inputData.userId)?.username
                ?: throw IllegalStateException("ユーザーが見つかりません")

        val user = PublicKeyCredentialUserEntity(
                /* id = */          Base64UrlUtil.decode(inputData.userId.value()),
                /* name = */        username.value(),
                /* displayName = */ username.value(),
        )

        val authenticatorSelectionCriteria = AuthenticatorSelectionCriteria(
                /* authenticatorAttachment = */ null,
                /* requireResidentKey =      */ true,
                /* userVerification =        */ UserVerificationRequirement.REQUIRED
        )

        val options = PublicKeyCredentialCreationOptions(
                /* rp =                     */ PublicKeyCredentialRpEntity(environments.rpId, "SecureStream"),
                /* user =                   */ user,
                /* challenge =              */ challenge,
                /* pubKeyCredParams =       */ pubKeyCredParams,
                /* timeout =                */ TimeUnit.SECONDS.toMillis(6000),
                /* excludeCredentials =     */ null,
                /* authenticatorSelection = */ authenticatorSelectionCriteria,
                /* attestation =            */ AttestationConveyancePreference.NONE,
                /* extensions =             */ null,
        )

        val flow =
                Flow.of(userId = inputData.userId, challenge = challenge)

        flowRepository.save(flow)

        return RequestWebauthnOutputData(
                flowId = flow.flowId,
                options = options
        )
    }
}