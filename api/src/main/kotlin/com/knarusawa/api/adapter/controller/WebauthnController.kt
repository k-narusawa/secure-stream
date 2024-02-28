package com.knarusawa.api.adapter.controller

import com.knarusawa.api.adapter.exception.UnauthorizedException
import com.knarusawa.api.application.registerWebauthn.RegisterWebauthnInputData
import com.knarusawa.api.application.registerWebauthn.RegisterWebauthnService
import com.knarusawa.api.application.requestWebauthn.RequestWebauthnInputData
import com.knarusawa.api.application.requestWebauthn.RequestWebauthnService
import com.knarusawa.common.domain.flow.FlowId
import com.knarusawa.common.domain.user.UserId
import com.webauthn4j.util.Base64UrlUtil
import org.openapitools.api.ApiSecureStream
import org.openapitools.model.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class WebauthnController(
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
        println(outputData.options.pubKeyCredParams)
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
                        alg = it.alg.value,
                        type = it.type.value,
                    )
                },
                timeout = outputData.options.timeout?.toInt() ?: 0,
                excludeCredentials = outputData.options.excludeCredentials?.map {
                    RequestWebauthnRegistrationExcludeCredentialsInner(
                        id = Base64UrlUtil.encodeToString(it.id),
                        type = it.type.value,
                    )
                } ?: listOf(),
                authenticatorSelection = RequestWebauthnRegistrationAuthenticatorSelection(
                    authenticatorAttachment = outputData.options.authenticatorSelection?.authenticatorAttachment?.value,
                    requireResidentKey = outputData.options.authenticatorSelection?.isRequireResidentKey,
                    userVerification = outputData.options.authenticatorSelection?.userVerification?.value
                ),
                attestation = outputData.options.attestation?.value ?: "",
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


    override fun registerWebauthn(registerWebauthnRequest: RegisterWebauthnRequest): ResponseEntity<Unit> {
        val principal =
            SecurityContextHolder.getContext().authentication.principal as? OAuth2AuthenticatedPrincipal
        val userId = principal?.getAttribute<String?>("sub")
            ?: throw UnauthorizedException()

        val inputData = RegisterWebauthnInputData(
            userId = UserId.from(value = userId),
            flowId = FlowId.from(value = registerWebauthnRequest.flowId),
            id = registerWebauthnRequest.id,
            rawId = registerWebauthnRequest.rawId,
            type = registerWebauthnRequest.type,
            attestationObject = registerWebauthnRequest.response.attestationObject ?: "",
            clientDataJSON = registerWebauthnRequest.response.clientDataJSON ?: "",
        )
        registerWebauthnService.exec(inputData)
        return ResponseEntity(HttpStatus.OK)
    }

}