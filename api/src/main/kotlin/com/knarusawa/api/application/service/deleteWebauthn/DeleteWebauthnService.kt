package com.knarusawa.api.application.service.deleteWebauthn

import com.knarusawa.common.domain.webauthn.WebauthnCredentialsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteWebauthnService(
    private val webauthnCredentialsRepository: WebauthnCredentialsRepository
) {
    @Transactional
    fun exec(inputData: DeleteWebauthnInputData) {
        if (inputData.credentialId.isNullOrEmpty()) {
            webauthnCredentialsRepository.deleteByUserId(userId = inputData.userId)
        } else {
            webauthnCredentialsRepository.deleteByCredentialId(credentialId = inputData.credentialId)
        }
    }
}