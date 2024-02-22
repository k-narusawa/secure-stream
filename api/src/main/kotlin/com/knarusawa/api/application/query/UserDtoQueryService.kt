package com.knarusawa.api.application.query

import com.knarusawa.api.application.dto.UserDto

interface UserDtoQueryService {
    fun findByUserId(userId: String): UserDto?
}