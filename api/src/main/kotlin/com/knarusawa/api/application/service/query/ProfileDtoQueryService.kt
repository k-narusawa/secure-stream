package com.knarusawa.api.application.service.query

import com.knarusawa.api.application.service.dto.ProfileDto

interface ProfileDtoQueryService {
    fun findByUserId(userId: String): ProfileDto
}