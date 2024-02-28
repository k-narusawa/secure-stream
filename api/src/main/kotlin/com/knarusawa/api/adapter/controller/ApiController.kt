package com.knarusawa.api.adapter.controller

import com.knarusawa.api.adapter.exception.UnauthorizedException
import com.knarusawa.api.application.registerWebauthn.RegisterWebauthnService
import com.knarusawa.api.application.requestWebauthn.RequestWebauthnInputData
import com.knarusawa.api.application.requestWebauthn.RequestWebauthnService
import com.knarusawa.common.domain.user.UserId
import com.webauthn4j.util.Base64UrlUtil
import org.openapitools.api.ApiSecureStream
import org.openapitools.model.*
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal

class ApiController(
    private val requestWebauthnService: RequestWebauthnService,
    private val registerWebauthnService: RegisterWebauthnService,
) : ApiSecureStream {
    override fun requestWebauthnRegistration(): ResponseEntity<RequestWebauthnRegistration> {
        val principal =
            SecurityContextHolder.getContext().authentication.principal as? OAuth2AuthenticatedPrincipal

        val userId = principal?.getAttribute<String?>("sub")
            ?: throw UnauthorizedException()

        val inputData = RequestWebauthnInputData(userId = UserId.from(userId))

        val outputData = requestWebauthnService.exec(inputData)
        return ResponseEntity.ok(
            RequestWebauthnRegistration(
                flowId = outputData.flowId.value(),
                rp = RequestWebauthnRegistrationRp(
                    id = outputData.options.rp.id,
                    name = outputData.options.rp.name
                ),
                user = RequestWebauthnRegistrationUser(
                    id = Base64UrlUtil.encodeToString(outputData.options.user.id),
                    name = outputData.options.user.name,
                    displayName = outputData.options.user.displayName
                ),
                challenge = Base64UrlUtil.encodeToString(outputData.options.challenge.value),
                pubKeyCredParams = outputData.options.pubKeyCredParams.map {
                    RequestWebauthnRegistrationPubKeyCredParamsInner(
                        type = it.type.value,
                    )
                },
                timeout = outputData.options.timeout?.toInt() ?: 0,
                excludeCredentials = outputData.options.excludeCredentials?.map {
                    RequestWebauthnRegistrationPubKeyCredParamsInner(
                        type = it.type.value,
                    )
                } ?: listOf(),
                authenticatorSelection = RequestWebauthnRegistrationAuthenticatorSelection(
                    authenticatorAttachment = null,
                    requireResidentKey = null,
                    userVerification = null
                ),
                attestation = outputData.options.attestation.toString(),
                extensions = outputData.options.extensions?.let {
                    RequestWebauthnRegistrationExtensions(
                        appid = it.appid,
                        authnSel = null,
                        exts = null,
                    )
                }

            )
        )
    }

    override fun registerWebauthn(registerWebauthnRequest: RegisterWebauthnRequest?): ResponseEntity<Unit> {


    }

}