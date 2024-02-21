package com.knarusawa.common.adapter.gateway.dao

import com.knarusawa.common.adapter.gateway.record.ProfileRecord
import org.springframework.data.repository.CrudRepository

interface ProfileRecordDao : CrudRepository<ProfileRecord, String> {
    fun findByUserId(userId: String): ProfileRecord
}