package com.knarusawa.secure_stream.adapter.gateway.db.dao

import com.knarusawa.secure_stream.adapter.gateway.db.record.UserRecord
import org.springframework.data.repository.CrudRepository

interface UserRecordDao : CrudRepository<UserRecord, String> {
    fun findByUsername(username: String): UserRecord?
    fun findByUserId(userId: String): UserRecord?
}