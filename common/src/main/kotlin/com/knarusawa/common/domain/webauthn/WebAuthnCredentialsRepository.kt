package com.knarusawa.common.domain.webauthn

interface WebAuthnCredentialsRepository {
    fun save(credentials: WebAuthnCredentials)
    fun findByCredentialId(credentialId: String): WebAuthnCredentials?
    fun findByUserId(userId: String): List<WebAuthnCredentials>
}