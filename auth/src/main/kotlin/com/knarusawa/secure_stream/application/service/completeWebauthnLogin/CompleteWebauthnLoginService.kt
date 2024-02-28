package com.knarusawa.secure_stream.application.service.completeWebauthnLogin

import com.knarusawa.common.domain.flow.FlowId
import com.knarusawa.common.domain.flow.FlowRepository
import com.knarusawa.common.domain.user.UserId
import com.knarusawa.common.domain.webauthn.WebauthnCredentialsRepository
import com.knarusawa.secure_stream.application.exception.UserNotFoundException
import com.knarusawa.secure_stream.util.logger
import com.webauthn4j.WebAuthnManager
import com.webauthn4j.authenticator.AuthenticatorImpl
import com.webauthn4j.converter.AttestedCredentialDataConverter
import com.webauthn4j.converter.exception.DataConversionException
import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.data.AuthenticationParameters
import com.webauthn4j.data.AuthenticationRequest
import com.webauthn4j.data.client.Origin
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.server.ServerProperty
import com.webauthn4j.util.Base64UrlUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CompleteWebauthnLoginService(
    private val flowRepository: FlowRepository,
    private val webauthnCredentialsRepository: WebauthnCredentialsRepository,
) {
    companion object {
        private val log = logger()
        private const val PR_ID = "localhost"
        private val attestedCredentialDataConverter = AttestedCredentialDataConverter(ObjectConverter())
    }

    @Transactional
    fun exec(inputData: CompleteWebauthnLoginInputData): CompleteWebauthnLoginOutputData {
        println(inputData)
        val userId = inputData.userHandle?.let { UserId.from(it) }
            ?: throw UserNotFoundException("ユーザーの識別に失敗しました")

        val origin = Origin.create("http://localhost:3001")

        val flow = flowRepository.findByFlowId(FlowId.from(inputData.flowId))
            ?: throw IllegalArgumentException("flow is not found")

        val challenge = flow.let { Base64UrlUtil.decode(it.challenge) }

        val serverProperty = ServerProperty(origin, PR_ID, DefaultChallenge(challenge), null)

        val credentials = webauthnCredentialsRepository.findByCredentialId(inputData.credentialId)
            ?: throw IllegalArgumentException("credential is not found. id: [${inputData.credentialId}]")

        val authenticator = AuthenticatorImpl(
            /* attestedCredentialData = */
            attestedCredentialDataConverter.convert(
                Base64UrlUtil.decode(credentials.serializedAttestedCredentialData)
            ),
            /* attestationStatement = */   null,
            /* counter =              */   credentials.counter
        )

        val authenticationParameter = AuthenticationParameters(
            serverProperty,
            authenticator,
            listOf(Base64UrlUtil.decode(credentials.credentialId)),
            false
        )

        val authenticationRequest = AuthenticationRequest(
            /* credentialId = */      Base64UrlUtil.decode(inputData.credentialId),
            /* userHandle = */        inputData.userHandle?.let { Base64UrlUtil.decode(it) },
            /* authenticatorData = */ Base64UrlUtil.decode(inputData.authenticatorData),
            /* clientDataJSON = */    Base64UrlUtil.decode(inputData.clientDataJSON),
            /* signature = */         Base64UrlUtil.decode(inputData.signature),
        )

        val authenticationData = try {
            WebAuthnManager.createNonStrictWebAuthnManager().parse(authenticationRequest);
        } catch (ex: DataConversionException) {
            throw ex
        }

        try {
            WebAuthnManager.createNonStrictWebAuthnManager()
                .validate(authenticationRequest, authenticationParameter)
        } catch (ex: Exception) {
            throw ex
        }

        authenticationData.authenticatorData?.let {
            credentials.updateCounter(it.signCount)
            webauthnCredentialsRepository.save(credentials)
        } ?: log.warn("credentialsのカウント更新に失敗しました")

        return CompleteWebauthnLoginOutputData(userId = userId)
    }
}