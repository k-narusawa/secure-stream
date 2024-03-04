package com.knarusawa.common.domain.socialLoginState

import com.knarusawa.common.adapter.gateway.record.SocialLoginStateRecord
import com.knarusawa.common.domain.user.UserId
import java.time.LocalDateTime

class SocialLoginState private constructor(
    val state: State,
    val userId: UserId?,
    val challenge: String?,
    val expiredAt: LocalDateTime,
) {
    companion object {
        private const val STATE_LIFETIME_SEC = 60L
        fun of(userId: UserId): SocialLoginState {
            return SocialLoginState(
                state = State.of(),
                userId = userId,
                challenge = null,
                expiredAt = LocalDateTime.now().plusSeconds(STATE_LIFETIME_SEC)
            )
        }

        fun of(challenge: String): SocialLoginState {
            return SocialLoginState(
                state = State.of(),
                userId = null,
                challenge = challenge,
                expiredAt = LocalDateTime.now().plusSeconds(STATE_LIFETIME_SEC)
            )
        }

        fun from(record: SocialLoginStateRecord) = SocialLoginState(
            state = State.from(record.state),
            userId = record.userId?.let { UserId.from(record.userId) },
            challenge = record.challenge,
            expiredAt = record.expiredAt,
        )
    }

    fun validate(): Boolean {
        return this.expiredAt >= LocalDateTime.now()
    }
}