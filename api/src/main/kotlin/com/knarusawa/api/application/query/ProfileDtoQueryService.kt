package com.knarusawa.api.application.query

import com.knarusawa.api.application.dto.ProfileDto

interface ProfileDtoQueryService {
    fun findByUserId(userId: String): ProfileDto
}