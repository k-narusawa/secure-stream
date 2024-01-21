package com.knarusawa.secure_stream.domain.user

interface UserRepository {
    fun save(user: User)
    fun findByUsername(username: Username): User?
    fun findByUserId(userId: UserId): User?
}