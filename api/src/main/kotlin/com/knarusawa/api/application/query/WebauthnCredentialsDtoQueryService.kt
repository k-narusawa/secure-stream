package com.knarusawa.api.application.query

import com.knarusawa.api.application.dto.WebauthnCredentialsDto
import com.knarusawa.common.domain.user.UserId

interface WebauthnCredentialsDtoQueryService {
    fun findByUserId(userId: UserId): List<WebauthnCredentialsDto>
}