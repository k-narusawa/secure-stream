package com.knarusawa.common.domain.user

import java.util.*

data class UserId private constructor(
        private val value: UUID
) {
    companion object {
        fun of() = UserId(value = UUID.randomUUID())
        fun from(value: String): UserId {
            // 失敗したらIllegalArgumentExceptionが投げられる
            return UserId(UUID.fromString(value))
        }
    }

    fun value() = this.value.toString()

    override fun toString(): String {
        return this.value()
    }
}
