package com.knarusawa.common.adapter.gateway.api

import com.knarusawa.common.adapter.gateway.api.dto.LoginOauthAccessTokenBody
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.retry.Retry
import java.time.Duration

@Component
class GitHubWebClient {
    companion object {
        private val client = WebClient.builder()
            .baseUrl("https://github.com")
            .codecs { codecs ->
                codecs
                    .defaultCodecs()
                    .maxInMemorySize(500 * 1024)
            }
            .build()
    }

    fun loginOauthAccessToken(
        clientId: String,
        clientSecret: String,
        code: String,
        redirectUri: String
    ): LoginOauthAccessTokenBody {
        val map: MultiValueMap<String, String> = LinkedMultiValueMap()
        map.add("client_id", clientId)
        map.add("client_secret", clientSecret)
        map.add("code", code)
        map.add("redirect_uri", redirectUri)
        map.add("grant_type", "authorization_code")

        return client.post()
            .uri("/login/oauth/access_token")
            .body(BodyInserters.fromFormData(map))
            .accept(APPLICATION_JSON)
            .retrieve()
            .bodyToMono(LoginOauthAccessTokenBody::class.java).log()
            .checkpoint("チェックポイント", true)
            .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(500)))
            .block()
            ?: throw RuntimeException("外部APIリクエスト時に失敗しました")
    }
}