package com.knarusawa.common.domain.socialLogin

enum class Provider(val value: String) {
    GOOGLE("google"),
    GITHUB("github"),
    ;

    companion object {
        fun from(value: String) = entries.find { it.value == value }
            ?: throw IllegalStateException("想定外のプロバイダです")
    }

    fun value() = this.value
}
