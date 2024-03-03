package com.knarusawa.common.domain.socialLoginState

import java.math.BigInteger
import java.security.SecureRandom

data class State private constructor(
    val value: String
) {
    companion object {
        fun of(): State {
            val random = SecureRandom()
            val state = BigInteger(130, random).toString(32)
            return State(value = state)
        }

        fun from(value: String) = State(value)
    }

}
