package com.knarusawa.secure_stream.domain.profile

data class GivenName private constructor(
        val value: String
) {
    companion object {
        private val LENGTH_RANGE = (1..255)

        fun of(value: String): GivenName {
            return value
                    .takeIf { LENGTH_RANGE.contains(value = it.length) }
                    ?.let { GivenName(value = value) }
                    ?: throw IllegalArgumentException("given_nameの値が不正です")
        }
    }

    fun value() = this.value.toString()
}