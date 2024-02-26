package com.knarusawa.common.adapter.gateway.dao

import com.knarusawa.common.adapter.gateway.record.WebauthnCredentialsRecord
import org.springframework.data.repository.CrudRepository

interface WebauthnCredentialsRecordDao : CrudRepository<WebauthnCredentialsRecord, String> {
    fun findByCredentialId(credentialId: String): WebauthnCredentialsRecord?
    fun findByUserId(userId: String): List<WebauthnCredentialsRecord>
}