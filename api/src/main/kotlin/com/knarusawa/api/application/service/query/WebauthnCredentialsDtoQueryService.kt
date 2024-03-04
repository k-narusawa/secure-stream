package com.knarusawa.api.application.service.query

import com.knarusawa.api.application.service.dto.WebauthnCredentialsDto
import com.knarusawa.common.domain.user.UserId

interface WebauthnCredentialsDtoQueryService {
    fun findByUserId(userId: UserId): List<WebauthnCredentialsDto>
}