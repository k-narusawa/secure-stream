package com.knarusawa.secure_stream.domain.profile

import com.knarusawa.secure_stream.domain.user.UserId

interface ProfileRepository {
    fun save(profile: Profile)
    fun findByUserId(userId: UserId): Profile
}