package com.knarusawa.api.adapter.controller.dto

data class Profile(
    val familyName: String,
    val givenName: String,
    val nickname: String?,
    val picture: String?,
)