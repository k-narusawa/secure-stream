package com.knarusawa.api.application.service.dto

data class ProfileDto(
    val userId: String,
    val familyName: String,
    val givenName: String,
    val nickname: String?,
    val picture: String?,
)