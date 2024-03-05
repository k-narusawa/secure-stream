package com.knarusawa.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class Environments {
    @Value("\${env.cors.allowed-origin}")
    lateinit var allowedOrigin: String

    @Value("\${env.webauthn.rp.id}")
    lateinit var rpId: String

    @Value("\${env.auth-front.url}")
    lateinit var authFrontUrl: String

    @Value("\${env.sp-front.url}")
    lateinit var spFrontUrl: String
}