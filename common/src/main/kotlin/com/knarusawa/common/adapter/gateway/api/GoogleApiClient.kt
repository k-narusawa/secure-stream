package com.knarusawa.common.adapter.gateway.api

import com.knarusawa.common.adapter.gateway.api.dto.GitHubAccessTokenResponse
import com.knarusawa.common.adapter.gateway.api.dto.GoogleProfile
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.retry.Retry
import java.time.Duration

@Component
class GoogleApiClient(
    private val loggingWebFilter: LoggingWebFilter
) {
    companion object {
        private val client = WebClient.builder()
            .baseUrl("https://www.googleapis.com")
            .codecs { codecs ->
                codecs
                    .defaultCodecs()
                    .maxInMemorySize(500 * 1024)
            }
            .filter(WebClientConfig().logRequest())
            .filter(WebClientConfig().logResponse())
            .build()
    }

    fun loginOauthAccessToken(
        clientId: String,
        clientSecret: String,
        code: String,
        redirectUri: String,
        scope: String,
    ): GitHubAccessTokenResponse {
        val map: MultiValueMap<String, String> = LinkedMultiValueMap()
        map.add("client_id", clientId)
        map.add("client_secret", clientSecret)
        map.add("code", code)
        map.add("redirect_uri", redirectUri)
        map.add("scope", scope)
        map.add("grant_type", "authorization_code")

        return client.post()
            .uri("/oauth2/v4/token")
            .body(BodyInserters.fromFormData(map))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(GitHubAccessTokenResponse::class.java)
            .retryWhen(Retry.fixedDelay(1, Duration.ofMillis(500)))
            .block()
            ?: throw RuntimeException("外部APIリクエスト時に失敗しました")
    }

    fun profile(accessToken: String): GoogleProfile {
        return client.get()
            .uri("/oauth2/v1/userinfo")
            .header("Authorization", "Bearer $accessToken")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(GoogleProfile::class.java).log()
            .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(500)))
            .block()
            ?: throw RuntimeException("外部APIリクエスト時に失敗しました")

    }
}