package com.knarusawa.secure_stream.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class Environments {
    @Value("\${env.cors.allowed-origin}")
    lateinit var allowedOrigin: String

    @Value("\${env.hydra.admin.url}")
    lateinit var hydraAdminUrl: String
}