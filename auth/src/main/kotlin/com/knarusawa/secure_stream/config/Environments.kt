package com.knarusawa.secure_stream.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class Environments {
    @Value("\${env.cors.allowed-origin}")
    lateinit var allowedOrigin: String

    @Value("\${env.hydra.admin.url}")
    lateinit var hydraAdminUrl: String

    @Value("\${env.hydra.login.remember-for}")
    lateinit var loginRememberFor: String

    @Value("\${env.hydra.consent.remember-for}")
    lateinit var consentRememberFor: String

    @Value("\${env.auth-front.url}")
    lateinit var authFrontUrl: String

    @Value("\${env.sp-front.url}")
    lateinit var spFrontUrl: String
}