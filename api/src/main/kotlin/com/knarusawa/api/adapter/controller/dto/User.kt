package com.knarusawa.api.adapter.controller.dto

data class User(
        val username: String,
        val isAccountLock: Boolean,
        val mfa: Boolean,
        val passkey: Boolean
)
