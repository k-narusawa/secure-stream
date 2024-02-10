package com.knarusawa.secure_stream.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import sh.ory.hydra.ApiClient
import sh.ory.hydra.api.OAuth2Api

@Configuration
class ApiConfig {
    @Bean
    fun oAuth2Api(): OAuth2Api {
        val apiClient = ApiClient().apply {
            this.setBasePath("http://localhost:4445")
        }

        return OAuth2Api(apiClient)
    }

    @Bean
    fun webClientBuilder(): WebClient.Builder {
        return WebClient.builder()
    }
}