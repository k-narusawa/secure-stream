package com.knarusawa.common.domain.user

interface UserRepository {
    fun save(user: User)
    fun findByUsername(username: Username): User?
    fun findByUserId(userId: UserId): User?
}