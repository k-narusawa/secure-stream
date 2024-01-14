package com.knarusawa.secure_stream.adapter.gateway.db.dao

import com.knarusawa.secure_stream.adapter.gateway.db.record.UserRecord
import org.springframework.data.repository.CrudRepository

interface UsersDao : CrudRepository<UserRecord, String> {
    fun findByUsername(username: String): UserRecord?
}