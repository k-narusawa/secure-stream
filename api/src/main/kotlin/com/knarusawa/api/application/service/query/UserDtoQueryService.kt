package com.knarusawa.api.application.service.query

import com.knarusawa.api.application.service.dto.UserDto

interface UserDtoQueryService {
    fun findByUserId(userId: String): UserDto?
}