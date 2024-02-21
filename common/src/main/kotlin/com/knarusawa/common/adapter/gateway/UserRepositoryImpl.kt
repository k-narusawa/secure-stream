package com.knarusawa.common.adapter.gateway

import com.knarusawa.common.adapter.gateway.dao.UserRecordDao
import com.knarusawa.common.adapter.gateway.record.UserRecord
import com.knarusawa.common.domain.user.User
import com.knarusawa.common.domain.user.UserId
import com.knarusawa.common.domain.user.UserRepository
import com.knarusawa.common.domain.user.Username
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
        private val userRecordDao: UserRecordDao
) : UserRepository {
    override fun save(user: User) {
        userRecordDao.save(UserRecord(
                userId = user.userId.value(),
                username = user.username.value(),
                password = user.password.value(),
                isAccountLock = user.isAccountLock,
                failedAttempts = user.failedAttempts,
                isDisabled = user.isDisabled,
        ))
    }

    override fun findByUsername(username: Username): User? {
        val user = userRecordDao.findByUsername(username = username.value())
        return user?.let { User.from(it) }
    }

    override fun findByUserId(userId: UserId): User? {
        val user = userRecordDao.findByUserId(userId = userId.value())
        return user?.let { User.from(it) }
    }
}