package com.knarusawa.api.application.service.dto

data class UserDto(
    val userId: String,
    val username: String,
    val isAccountLock: Boolean,
)