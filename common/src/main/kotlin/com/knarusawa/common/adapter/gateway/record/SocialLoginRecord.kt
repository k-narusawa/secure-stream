package com.knarusawa.common.adapter.gateway.record

import com.knarusawa.common.domain.socialLogin.SocialLogin
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "social_login")
@IdClass(value = SocialLoginKey::class)
data class SocialLoginRecord(
    @Id
    @Column(name = "user_id")
    val userId: String = "",

    @Id
    @Column(name = "provider")
    val provider: String = "",

    @Column(name = "sub")
    val sub: String = "",

    @Column(name = "created_at", insertable = false, updatable = false)
    val createdAt: LocalDateTime? = null,

    @Column(name = "updated_at", insertable = false, updatable = false)
    val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun from(socialLogin: SocialLogin) = SocialLoginRecord(
            userId = socialLogin.userId.value(),
            provider = socialLogin.provider.value,
            sub = socialLogin.sub,
        )
    }
}