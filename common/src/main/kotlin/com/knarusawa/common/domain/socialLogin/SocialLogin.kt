package com.knarusawa.common.domain.socialLogin

import com.knarusawa.common.adapter.gateway.record.SocialLoginRecord
import com.knarusawa.common.domain.user.UserId

class SocialLogin private constructor(
    val userId: UserId,
    val provider: Provider,
    val sub: String,
) {
    companion object {
        fun of(userId: UserId, provider: Provider, sub: String) = SocialLogin(
            userId = userId,
            provider = provider,
            sub = sub
        )

        fun from(record: SocialLoginRecord) = SocialLogin(
            userId = UserId.from(record.userId),
            provider = Provider.from(value = record.provider),
            sub = record.sub
        )
    }
}