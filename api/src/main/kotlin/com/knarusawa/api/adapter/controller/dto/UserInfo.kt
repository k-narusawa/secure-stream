package com.knarusawa.api.adapter.controller.dto

data class UserInfo(
    val userId: String,
    val user: User?,
    val profile: Profile?
)