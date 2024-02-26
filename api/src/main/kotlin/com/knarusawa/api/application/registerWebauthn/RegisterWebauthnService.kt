package com.knarusawa.api.application.registerWebauthn

import com.knarusawa.api.config.Environments
import com.knarusawa.common.domain.flow.FlowRepository
import com.knarusawa.common.domain.webauthn.WebauthnCredentials
import com.knarusawa.common.domain.webauthn.WebauthnCredentialsRepository
import com.webauthn4j.WebAuthnManager
import com.webauthn4j.authenticator.AuthenticatorImpl
import com.webauthn4j.data.PublicKeyCredentialParameters
import com.webauthn4j.data.PublicKeyCredentialType
import com.webauthn4j.data.RegistrationParameters
import com.webauthn4j.data.RegistrationRequest
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier
import com.webauthn4j.data.client.Origin
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.server.ServerProperty
import com.webauthn4j.util.Base64UrlUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterWebauthnService(
        private val flowRepository: FlowRepository,
        private val webauthnCredentialsRepository: WebauthnCredentialsRepository,
        private val environments: Environments
) {
    @Transactional
    fun exec(inputData: RegisterWebauthnInputData) {
        val origin = Origin.create("http://localhost:3000")
        val flow =
                flowRepository.findByFlowId(inputData.flowId)

        val challenge = flow?.let { Base64UrlUtil.decode(it.challenge) }
        val attestationObject = Base64UrlUtil.decode(inputData.attestationObject)
        val clientDataJSON = Base64UrlUtil.decode(inputData.clientDataJSON)

        val pubKeys = listOf(
                PublicKeyCredentialParameters(
                        PublicKeyCredentialType.PUBLIC_KEY,
                        COSEAlgorithmIdentifier.ES256
                ),
                PublicKeyCredentialParameters(
                        PublicKeyCredentialType.PUBLIC_KEY,
                        COSEAlgorithmIdentifier.RS256
                ),
        )

        val serverProperty = ServerProperty(origin, environments.rpId, DefaultChallenge(challenge), null)
        val registrationRequest = RegistrationRequest(attestationObject, clientDataJSON)
        val registrationParameters = RegistrationParameters(serverProperty, pubKeys, true)

        val registrationData =
                WebAuthnManager.createNonStrictWebAuthnManager().parse(registrationRequest);

        WebAuthnManager.createNonStrictWebAuthnManager()
                .validate(registrationRequest, registrationParameters)


        if (
                registrationData.attestationObject == null ||
                registrationData.attestationObject!!.authenticatorData.attestedCredentialData == null
        ) {
            throw IllegalStateException("不正なデータ")
        }

        val authenticator = AuthenticatorImpl(
                /* attestedCredentialData = */
                registrationData.attestationObject!!.authenticatorData.attestedCredentialData!!,
                /* attestationStatement =   */
                registrationData.attestationObject!!.attestationStatement,
                /* counter =                */
                registrationData.attestationObject!!.authenticatorData.signCount,
        )

        val credentialId =
                registrationData.attestationObject!!.authenticatorData.attestedCredentialData!!.credentialId

        val credentials = WebauthnCredentials.of(
                credentialId = credentialId,
                userId = inputData.userId.value(),
                authenticator = authenticator,
        )

        webauthnCredentialsRepository.save(credentials)
        flowRepository.deleteByUserId(inputData.userId)
    }
}