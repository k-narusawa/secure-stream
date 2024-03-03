package com.knarusawa.common.domain.socialLogin

import com.knarusawa.common.domain.user.UserId

interface SocialLoginRepository {
    fun save(socialLogin: SocialLogin)
    fun findByUserId(userId: UserId): List<SocialLogin>
    fun deleteByUserIdAndProvider(userId: UserId, provider: Provider)
}