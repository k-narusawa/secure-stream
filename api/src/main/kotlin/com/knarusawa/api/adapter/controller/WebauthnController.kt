package com.knarusawa.api.adapter.controller

import com.knarusawa.api.adapter.controller.dto.ApiV1WebauthnPostRequest
import com.knarusawa.api.adapter.controller.dto.ApiV1WebauthnRequestGetResponse
import com.knarusawa.api.adapter.exception.UnauthorizedException
import com.knarusawa.api.application.registerWebauthn.RegisterWebauthnInputData
import com.knarusawa.api.application.registerWebauthn.RegisterWebauthnService
import com.knarusawa.api.application.requestWebauthn.RequestWebauthnInputData
import com.knarusawa.api.application.requestWebauthn.RequestWebauthnService
import com.knarusawa.common.domain.flow.FlowId
import com.knarusawa.common.domain.user.UserId
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/webauthn")
class WebauthnController(
        private val requestWebauthnService: RequestWebauthnService,
        private val registerWebauthnService: RegisterWebauthnService,
) {
    @GetMapping("/request")
    fun apiV1WebauthnRequestGet(
            @AuthenticationPrincipal principal: OAuth2AuthenticatedPrincipal
    ): ApiV1WebauthnRequestGetResponse {
        val userId = principal.getAttribute<String?>("sub")
                ?: throw UnauthorizedException()

        val inputData = RequestWebauthnInputData(userId = UserId.from(userId))

        val outputData = requestWebauthnService.exec(inputData)

        return ApiV1WebauthnRequestGetResponse.from(flowId = outputData.flowId, options = outputData.options)
    }

    @PostMapping
    fun apiC1WebauthnPost(
            @AuthenticationPrincipal principal: OAuth2AuthenticatedPrincipal,
            @RequestBody body: ApiV1WebauthnPostRequest
    ) {
        val userId = principal.getAttribute<String?>("sub")
                ?: throw UnauthorizedException()

        val inputData = RegisterWebauthnInputData(
                userId = UserId.from(value = userId),
                flowId = FlowId.from(value = body.flowId),
                id = body.id,
                rawId = body.rawId,
                type = body.type,
                attestationObject = body.response.attestationObject,
                clientDataJSON = body.response.clientDataJSON,
        )
        registerWebauthnService.exec(inputData)
    }
}