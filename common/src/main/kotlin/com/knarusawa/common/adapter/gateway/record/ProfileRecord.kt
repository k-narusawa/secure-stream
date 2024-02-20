package com.knarusawa.common.adapter.gateway.record

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "profiles")
data class ProfileRecord(
    @Id
    @Column(name = "user_id")
    val userId: String = "",

    @Column(name = "family_name")
    val familyName: String = "",

    @Column(name = "given_name")
    val givenName: String = "",

    @Column(name = "nickname")
    val nickname: String? = null,

    @Column(name = "picture")
    val picture: String? = null,

    @Column(name = "created_at", insertable = false, updatable = false)
    val createdAt: LocalDateTime? = null,

    @Column(name = "updated_at", insertable = false, updatable = false)
    val updatedAt: LocalDateTime? = null
)