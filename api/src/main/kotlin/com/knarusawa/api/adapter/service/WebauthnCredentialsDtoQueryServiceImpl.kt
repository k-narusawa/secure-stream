package com.knarusawa.api.adapter.service

import com.knarusawa.api.application.service.dto.WebauthnCredentialsDto
import com.knarusawa.api.application.service.query.WebauthnCredentialsDtoQueryService
import com.knarusawa.common.adapter.gateway.dao.WebauthnCredentialsRecordDao
import com.knarusawa.common.domain.user.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class WebauthnCredentialsDtoQueryServiceImpl(
    private val webatuhnCredentialsRecordDao: WebauthnCredentialsRecordDao
) : WebauthnCredentialsDtoQueryService {
    override fun findByUserId(userId: UserId): List<WebauthnCredentialsDto> {
        val records = webatuhnCredentialsRecordDao.findByUserId(userId = userId.value())

        return records.map {
            WebauthnCredentialsDto(
                credentialId = it.credentialId,
                userId = it.userId,
                serializedAttestedCredentialData = it.serializedAttestedCredentialData,
                serializedEnvelope = it.serializedEnvelope,
                serializedTransports = it.serializedTransports,
                serializedAuthenticatorExtensions = it.serializedAuthenticatorExtensions,
                serializedClientExtensions = it.serializedClientExtensions,
                counter = it.counter
            )
        }
    }
}