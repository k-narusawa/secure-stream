package com.knarusawa.api.adapter.service

import com.knarusawa.api.application.service.dto.UserDto
import com.knarusawa.api.application.service.query.UserDtoQueryService
import com.knarusawa.common.adapter.gateway.dao.UserRecordDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserDtoQueryServiceImpl(
    private val userRecordDao: UserRecordDao
) : UserDtoQueryService {
    override fun findByUserId(userId: String): UserDto? {
        return userRecordDao.findByUserId(userId)?.let {
            return UserDto(
                userId = it.userId,
                username = it.username,
                isAccountLock = it.isAccountLock
            )
        }
    }
}