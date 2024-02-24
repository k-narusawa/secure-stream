package com.knarusawa.api.adapter.controller

import com.knarusawa.api.adapter.controller.dto.ApiV1WebauthnRequestGetResponse
import com.knarusawa.api.adapter.exception.UnauthorizedException
import com.knarusawa.api.application.requestWebauthn.RequestWebauthnInputData
import com.knarusawa.api.application.requestWebauthn.RequestWebauthnService
import com.knarusawa.common.domain.user.UserId
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/webauthn")
class WebauthnController(
        private val requestWebauthnService: RequestWebauthnService
) {
    @GetMapping("/request")
    fun apiV1WebauthnRequestGet(
            @AuthenticationPrincipal principal: OAuth2AuthenticatedPrincipal
    ): ApiV1WebauthnRequestGetResponse {
        val userId = principal.getAttribute<String?>("sub")
                ?: throw UnauthorizedException()

        val inputData = RequestWebauthnInputData(
                userId = UserId.from(userId),
                authenticatorAttachment = RequestWebauthnInputData.AuthenticatorAttachment.CROSS_PLATFORM
        )

        val outputData = requestWebauthnService.exec(inputData)

        return ApiV1WebauthnRequestGetResponse.from(flowId = outputData.flowId, options = outputData.options)
    }
}