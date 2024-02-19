package com.knarusawa.secure_stream.domain.profile

data class Nickname(
        val value: String
) {
    companion object {
        private val LENGTH_RANGE = (1..255)

        fun of(value: String): Nickname {
            return value
                    .takeIf { LENGTH_RANGE.contains(value = it.length) }
                    ?.let { Nickname(value = value) }
                    ?: throw IllegalArgumentException("nicknameの値が不正です")
        }
    }

    fun value() = this.value.toString()
}
