package com.knarusawa.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class Environments {
    @Value("\${env.cors.allowed-origin}")
    lateinit var allowedOrigin: String

    @Value("\${env.webauthn.rp.id}")
    lateinit var rpId: String

    @Value("\${env.url.auth-front}")
    lateinit var authFrontUrl: String

    @Value("\${env.url.sp-front}")
    lateinit var spFrontUrl: String
}