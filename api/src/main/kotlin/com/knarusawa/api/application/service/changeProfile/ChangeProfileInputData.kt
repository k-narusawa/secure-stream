package com.knarusawa.api.application.service.changeProfile

data class ChangeProfileInputData(
    val userId: String,
    val familyName: String,
    val givenName: String,
    val nickname: String?,
    val picture: String?
)
