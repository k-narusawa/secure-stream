package com.knarusawa.secure_stream.adapter.controller

import com.knarusawa.secure_stream.adapter.controller.response.ApiV1LoginWebauthnRequestGetResponse
import com.knarusawa.secure_stream.application.service.requestWebauthnLogin.RequestWebauthnLoginService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/login/webauthn")
class WebauthnLoginController(
        private val requestWebauthnLoginService: RequestWebauthnLoginService
) {
    @GetMapping("/request")
    fun apiV1LoginWebauthnRequestGet(): ApiV1LoginWebauthnRequestGetResponse {
        val outputData = requestWebauthnLoginService.exec()

        return ApiV1LoginWebauthnRequestGetResponse.from(
                flowId = outputData.flowId.value(),
                options = outputData.options
        )
    }
}