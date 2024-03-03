package com.knarusawa.common.adapter.gateway.dao

import com.knarusawa.common.adapter.gateway.record.SocialLoginRecord
import org.springframework.data.repository.CrudRepository

interface SocialLoginRecordDao : CrudRepository<SocialLoginRecord, String> {
    fun findByUserId(userId: String): List<SocialLoginRecord>
    fun deleteByUserIdAndProvider(userId: String, provider: String)
}