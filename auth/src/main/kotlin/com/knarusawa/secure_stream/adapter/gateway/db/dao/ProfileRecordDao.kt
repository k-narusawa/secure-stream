package com.knarusawa.secure_stream.adapter.gateway.db.dao

import com.knarusawa.secure_stream.adapter.gateway.db.record.ProfileRecord
import org.springframework.data.repository.CrudRepository

interface ProfileRecordDao : CrudRepository<ProfileRecord, String> {
    fun findByUserId(userId: String): ProfileRecord
}