package com.knarusawa.common.adapter.gateway.record

import com.knarusawa.common.domain.socialLoginState.SocialLoginState
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "social_login_state")
data class SocialLoginStateRecord(
    @Id
    @Column(name = "state")
    val state: String = "",

    @Column(name = "user_id")
    val userId: String? = null,

    @Column(name = "challenge")
    val challenge: String? = null,

    @Column(name = "expired_at")
    val expiredAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "created_at", insertable = false, updatable = false)
    val createdAt: LocalDateTime? = null,

    @Column(name = "updated_at", insertable = false, updatable = false)
    val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun from(socialLoginState: SocialLoginState) = SocialLoginStateRecord(
            state = socialLoginState.state.value,
            userId = socialLoginState.userId?.value(),
            challenge = socialLoginState.challenge,
            expiredAt = socialLoginState.expiredAt,
        )
    }
}