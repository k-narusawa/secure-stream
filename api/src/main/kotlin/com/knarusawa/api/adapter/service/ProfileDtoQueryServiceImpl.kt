package com.knarusawa.api.adapter.service

import com.knarusawa.api.application.dto.ProfileDto
import com.knarusawa.api.application.query.ProfileDtoQueryService
import com.knarusawa.common.adapter.gateway.dao.ProfileRecordDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProfileDtoQueryServiceImpl(
    private val profileRecordDao: ProfileRecordDao
) : ProfileDtoQueryService {
    override fun findByUserId(userId: String): ProfileDto {
        val record = profileRecordDao.findByUserId(userId)
        
        return ProfileDto(
            userId = record.userId,
            familyName = record.familyName,
            givenName = record.givenName,
            nickname = record.nickname,
            picture = record.picture
        )
    }
}