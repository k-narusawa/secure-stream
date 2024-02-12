package com.knarusawa.secure_stream.adapter.controller

import com.knarusawa.secure_stream.adapter.controller.response.ApiV1ConsentGetResponse
import com.knarusawa.secure_stream.adapter.controller.response.ApiV1ConsentPostResponse
import com.knarusawa.secure_stream.util.logger
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.*
import sh.ory.hydra.api.OAuth2Api
import sh.ory.hydra.model.AcceptOAuth2ConsentRequest

@RestController
@RequestMapping("/api/v1/consent")
class ConsentController(
        private val oAuth2Api: OAuth2Api,
) {
    companion object {
        private val log = logger()
    }

    @GetMapping
    fun apiV1ConsentGet(
            csrfToken: CsrfToken,
            @RequestParam("consent_challenge") consentChallenge: String?
    ): ApiV1ConsentGetResponse {
        if (consentChallenge.isNullOrEmpty()) {
            throw IllegalStateException("Challengeが見つかりません")
        }

        val res = oAuth2Api.getOAuth2ConsentRequest(consentChallenge)
        val scopes = res.requestedScope?.toList()

        if (scopes == null || res.skip == true) {
            val consentRequest = AcceptOAuth2ConsentRequest().apply {
                grantScope = res.requestedScope
                grantAccessTokenAudience = res.requestedAccessTokenAudience
            }

            val res = oAuth2Api.acceptOAuth2ConsentRequest(consentChallenge, consentRequest)

            return ApiV1ConsentGetResponse(
                    challenge = consentChallenge,
                    scopes = listOf(),
                    csrfToken = csrfToken.token.toString(),
                    redirectTo = res.redirectTo
            )
        }

        return ApiV1ConsentGetResponse(
                challenge = res.challenge,
                scopes = scopes.map {
                    when (it) {
                        "openid" -> ApiV1ConsentGetResponse.Scope(name = it, required = true, isChecked = false)
                        "offline" -> ApiV1ConsentGetResponse.Scope(name = it, required = true, isChecked = false)
                        else -> ApiV1ConsentGetResponse.Scope(name = it, required = false, isChecked = false)
                    }
                },
                csrfToken = csrfToken.token.toString(),
                redirectTo = null
        )
    }

    @PostMapping
    fun apiV1ConsentPost(
            @RequestParam("scopes[]") scopes: List<String>,
            @RequestParam("challenge") challenge: String
    ): ApiV1ConsentPostResponse {
        val consentRequest = AcceptOAuth2ConsentRequest().apply {
            grantScope = scopes
            remember = true
            rememberFor = 3600
        }

        val res = oAuth2Api.acceptOAuth2ConsentRequest(challenge, consentRequest)

        return ApiV1ConsentPostResponse(res.redirectTo)
    }
}