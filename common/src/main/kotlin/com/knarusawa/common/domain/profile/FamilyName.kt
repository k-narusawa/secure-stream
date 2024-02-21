package com.knarusawa.common.domain.profile

data class FamilyName private constructor(
    val value: String
) {
    companion object {
        private val LENGTH_RANGE = (1..255)

        fun of(value: String): FamilyName {
            return value
                .takeIf { LENGTH_RANGE.contains(value = it.length) }
                ?.let { FamilyName(value = value) }
                ?: throw IllegalArgumentException("family_nameの値が不正です")
        }
    }

    fun value() = this.value.toString()
}