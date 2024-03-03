package com.knarusawa.common.domain.socialLogin

enum class Provider(val value: String) {
    GOOGLE("google"),
    GITHUB("github"),
    ;

    companion object {
        fun from(value: String) = Provider.valueOf(value = value)
    }

    fun value() = this.value
}
