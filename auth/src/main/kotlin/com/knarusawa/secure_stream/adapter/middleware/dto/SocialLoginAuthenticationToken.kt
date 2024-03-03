package com.knarusawa.secure_stream.adapter.middleware.dto

import org.springframework.security.authentication.AbstractAuthenticationToken

class SocialLoginAuthenticationToken(
    val principal: SocialLoginPrincipal,
    val credentials: String,
) : AbstractAuthenticationToken(listOf()) {
    override fun getCredentials(): Any {
        return this.credentials
    }

    override fun getPrincipal(): Any {
        return this.principal
    }

    data class SocialLoginPrincipal(
        val provider: String,
        val state: String
    )
}