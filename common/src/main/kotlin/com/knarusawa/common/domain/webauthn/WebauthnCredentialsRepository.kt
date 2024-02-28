package com.knarusawa.common.domain.webauthn

import com.knarusawa.common.domain.user.UserId

interface WebauthnCredentialsRepository {
    fun save(credentials: WebauthnCredentials)
    fun findByCredentialId(credentialId: String): WebauthnCredentials?
    fun findByUserId(userId: String): List<WebauthnCredentials>
    fun deleteByUserId(userId: UserId)
}