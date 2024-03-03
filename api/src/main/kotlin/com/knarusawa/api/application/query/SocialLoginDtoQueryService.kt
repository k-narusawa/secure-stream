package com.knarusawa.api.application.query

import com.knarusawa.api.application.dto.SocialLoginDto

interface SocialLoginDtoQueryService {
    fun findByUserId(userId: String): SocialLoginDto
}