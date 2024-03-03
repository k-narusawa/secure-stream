package com.knarusawa.common.adapter.gateway.api

import com.knarusawa.common.adapter.gateway.api.dto.GitHubUser
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class GitHubApiWebClient {
    companion object {
        private val client = WebClient.builder()
            .baseUrl("https://api.github.com")
            .build()
    }

    fun user(accessToken: String): GitHubUser {
        return client.get()
            .uri("/user")
            .header("Authorization", "Bearer $accessToken")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(GitHubUser::class.java).log()
            .block()
            ?: throw RuntimeException("外部APIリクエスト時に失敗しました")

    }
}