package com.knarusawa.secure_stream.adapter.controller

import com.knarusawa.common.util.logger
import com.knarusawa.secure_stream.adapter.controller.response.ApiV1ConsentGetResponse
import com.knarusawa.secure_stream.adapter.controller.response.ApiV1ConsentPostResponse
import com.knarusawa.secure_stream.config.Environments
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.*
import sh.ory.hydra.api.OAuth2Api
import sh.ory.hydra.model.AcceptOAuth2ConsentRequest

@RestController
@RequestMapping("/api/v1/consent")
class ConsentController(
    private val oAuth2Api: OAuth2Api,
    private val environments: Environments
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

        val oauth2ConsentRequest = oAuth2Api.getOAuth2ConsentRequest(consentChallenge)
        val scopes = oauth2ConsentRequest.requestedScope?.toList()

        if (scopes == null || oauth2ConsentRequest.skip == true) {
            val consentRequest = AcceptOAuth2ConsentRequest().apply {
                grantScope = oauth2ConsentRequest.requestedScope
                grantAccessTokenAudience = oauth2ConsentRequest.requestedAccessTokenAudience
            }

            val oauth2RedirectTo =
                oAuth2Api.acceptOAuth2ConsentRequest(consentChallenge, consentRequest)

            return ApiV1ConsentGetResponse(
                challenge = consentChallenge,
                scopes = listOf(),
                csrfToken = csrfToken.token.toString(),
                redirectTo = oauth2RedirectTo.redirectTo
            )
        }

        return ApiV1ConsentGetResponse(
            challenge = oauth2ConsentRequest.challenge,
            scopes = scopes.map {
                when (it) {
                    "openid" -> ApiV1ConsentGetResponse.Scope(
                        name = it,
                        required = true,
                        isChecked = false
                    )

                    "offline" -> ApiV1ConsentGetResponse.Scope(
                        name = it,
                        required = true,
                        isChecked = false
                    )

                    else -> ApiV1ConsentGetResponse.Scope(
                        name = it,
                        required = false,
                        isChecked = false
                    )
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
            rememberFor = environments.consentRememberFor.toLong()
        }

        val res = oAuth2Api.acceptOAuth2ConsentRequest(challenge, consentRequest)

        return ApiV1ConsentPostResponse(res.redirectTo)
    }
}