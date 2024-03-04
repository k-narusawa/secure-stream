package com.knarusawa.api.application.service.query

import com.knarusawa.api.application.service.dto.SocialLoginDto

interface SocialLoginDtoQueryService {
    fun findByUserId(userId: String): SocialLoginDto
}