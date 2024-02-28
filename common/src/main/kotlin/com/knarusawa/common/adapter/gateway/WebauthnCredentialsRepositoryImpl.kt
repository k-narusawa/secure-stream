package com.knarusawa.common.adapter.gateway

import com.knarusawa.common.adapter.gateway.dao.WebauthnCredentialsRecordDao
import com.knarusawa.common.adapter.gateway.record.WebauthnCredentialsRecord
import com.knarusawa.common.domain.user.UserId
import com.knarusawa.common.domain.webauthn.WebauthnCredentials
import com.knarusawa.common.domain.webauthn.WebauthnCredentialsRepository
import org.springframework.stereotype.Repository

@Repository
class WebauthnCredentialsRepositoryImpl(
    private val webAuthnCredentialsRecordDao: WebauthnCredentialsRecordDao
) : WebauthnCredentialsRepository {
    override fun save(credentials: WebauthnCredentials) {
        webAuthnCredentialsRecordDao.save(WebauthnCredentialsRecord.from(credentials))
    }

    override fun findByCredentialId(credentialId: String): WebauthnCredentials? {
        val record = webAuthnCredentialsRecordDao.findByCredentialId(credentialId = credentialId)
        return record?.let {
            WebauthnCredentials.from(record)
        }
    }

    override fun findByUserId(userId: String): List<WebauthnCredentials> {
        val records = webAuthnCredentialsRecordDao.findByUserId(userId = userId)
        return records.map {
            WebauthnCredentials.from(it)
        }
    }

    override fun deleteByUserId(userId: UserId) {
        webAuthnCredentialsRecordDao.deleteByUserId(userId = userId.value())
    }
}