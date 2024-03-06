package com.knarusawa.secure_stream.adapter.controller

import com.knarusawa.secure_stream.adapter.controller.response.ApiV1LogoutResponse
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sh.ory.hydra.api.OAuth2Api

@RestController
@RequestMapping("/api/v1/logout")
class LogoutController(
    private val oAuth2Api: OAuth2Api,
) {
    @GetMapping
    fun apiV1Logout(
        @RequestParam("logout_challenge") logoutChallenge: String,
        csrfToken: CsrfToken,
    ): ApiV1LogoutResponse {
        val res = oAuth2Api.acceptOAuth2LogoutRequest(logoutChallenge)

        return ApiV1LogoutResponse(
            redirectTo = res.redirectTo
        )
    }
}