package com.knarusawa.common.adapter.gateway

import com.knarusawa.common.adapter.gateway.dao.WebAuthnCredentialsRecordDao
import com.knarusawa.common.adapter.gateway.record.WebAuthnCredentialsRecord
import com.knarusawa.common.domain.webauthn.WebAuthnCredentials
import com.knarusawa.common.domain.webauthn.WebAuthnCredentialsRepository
import org.springframework.stereotype.Repository

@Repository
class WebAuthnCredentialsRepositoryImpl(
        private val webAuthnCredentialsRecordDao: WebAuthnCredentialsRecordDao
) : WebAuthnCredentialsRepository {
    override fun save(credentials: WebAuthnCredentials) {
        webAuthnCredentialsRecordDao.save(WebAuthnCredentialsRecord.from(credentials))
    }

    override fun findByCredentialId(credentialId: String): WebAuthnCredentials? {
        TODO("Not yet implemented")
    }

    override fun findByUserId(userId: String): List<WebAuthnCredentials> {
        TODO("Not yet implemented")
    }
}