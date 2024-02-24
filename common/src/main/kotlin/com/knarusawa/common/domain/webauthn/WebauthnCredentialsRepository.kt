package com.knarusawa.common.domain.webauthn

interface WebauthnCredentialsRepository {
    fun save(credentials: WebauthnCredentials)
    fun findByCredentialId(credentialId: String): WebauthnCredentials?
    fun findByUserId(userId: String): List<WebauthnCredentials>
}