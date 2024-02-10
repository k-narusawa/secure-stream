package com.knarusawa.secure_stream.adapter.controller

import com.knarusawa.secure_stream.adapter.controller.response.ApiV1ConsentGetResponse
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sh.ory.hydra.ApiClient
import sh.ory.hydra.api.OAuth2Api

@RestController
@RequestMapping("/api/v1/consent")
class ConsentController {
    val apiClient = ApiClient().apply {
        this.setBasePath("http://localhost:4445")
    }
    val oauth2 = OAuth2Api(apiClient)

    @GetMapping
    fun apiV1ConsentGet(
            @Param("consent_challenge") consentChallenge: String?
    ): ApiV1ConsentGetResponse {
        if (consentChallenge.isNullOrEmpty()) {
            throw IllegalStateException("Challengeが見つかりません")
        }

        val res = oauth2.getOAuth2ConsentRequest(consentChallenge)
        val scopes = res.client?.scope?.split(" ") ?: listOf()

        return ApiV1ConsentGetResponse(
                challenge = res.challenge,
                scopes = scopes
        )
    }
}