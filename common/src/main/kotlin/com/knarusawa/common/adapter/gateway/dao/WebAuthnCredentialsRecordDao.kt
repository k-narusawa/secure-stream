package com.knarusawa.common.adapter.gateway.dao

import com.knarusawa.common.adapter.gateway.record.WebAuthnCredentialsRecord
import org.springframework.data.repository.CrudRepository

interface WebAuthnCredentialsRecordDao : CrudRepository<WebAuthnCredentialsRecord, String> {
    fun findByCredentialId(credentialId: String): WebAuthnCredentialsRecord?
    fun findByUserId(userId: String): List<WebAuthnCredentialsRecord>
}