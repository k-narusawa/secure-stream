package com.knarusawa.api.adapter.service

import com.knarusawa.api.application.service.dto.SocialLoginDto
import com.knarusawa.api.application.service.query.SocialLoginDtoQueryService
import com.knarusawa.common.adapter.gateway.dao.SocialLoginRecordDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SocialLoginDtoQueryServiceImpl(
    private val socialLoginRecordDao: SocialLoginRecordDao
) : SocialLoginDtoQueryService {
    override fun findByUserId(userId: String): SocialLoginDto {
        val records = socialLoginRecordDao.findByUserId(userId = userId)

        return SocialLoginDto(
            google = records.find { it.provider == "google" } != null,
            github = records.find { it.provider == "github" } != null,
        )
    }
}