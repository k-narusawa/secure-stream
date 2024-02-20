package com.knarusawa.common.domain.profile

interface ProfileRepository {
    fun save(profile: Profile)
    fun findByUserId(userId: String): Profile
}