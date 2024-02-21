package com.knarusawa.common.adapter.gateway.dao

import com.knarusawa.common.adapter.gateway.record.UserRecord
import org.springframework.data.repository.CrudRepository

interface UserRecordDao : CrudRepository<UserRecord, String> {
    fun findByUsername(username: String): UserRecord?
    fun findByUserId(userId: String): UserRecord?
}