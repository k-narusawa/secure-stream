package com.knarusawa.secure_stream.adapter.gateway.db

import com.knarusawa.secure_stream.adapter.gateway.db.dao.UsersDao
import com.knarusawa.secure_stream.adapter.gateway.db.record.UserRecord
import com.knarusawa.secure_stream.domain.user.User
import com.knarusawa.secure_stream.domain.user.UserId
import com.knarusawa.secure_stream.domain.user.UserRepository
import com.knarusawa.secure_stream.domain.user.Username
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
        private val usersDao: UsersDao
) : UserRepository {
    override fun save(user: User) {
        usersDao.save(UserRecord(
                userId = user.userId.value(),
                username = user.username.value(),
                password = user.password.value(),
                isAccountLock = user.isAccountLock,
                failedAttempts = user.failedAttempts,
                isDisabled = user.isDisabled,
        ))
    }

    override fun findByUsername(username: Username): User? {
        val user = usersDao.findByUsername(username = username.value())
        return user?.let { User.from(it) }
    }

    override fun findByUserId(userId: UserId): User? {
        val user = usersDao.findByUserId(userId = userId.value())
        return user?.let { User.from(it) }
    }
}