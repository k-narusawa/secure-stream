package com.knarusawa.api.application.service.changePassword

import com.knarusawa.api.application.exception.UserNotFoundException
import com.knarusawa.common.domain.user.UserId
import com.knarusawa.common.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sh.ory.hydra.api.OAuth2Api

@Service
class ChangePasswordService(
    private val userRepository: UserRepository,
    private val oauthApi: OAuth2Api
) {
    @Transactional
    fun exec(inputData: ChangePasswordInputData) {
        val user = userRepository.findByUserId(UserId.from(inputData.userId))
            ?: throw UserNotFoundException()

        user.changePassword(inputData.password)
        userRepository.save(user)

        oauthApi.revokeOAuth2ConsentSessions(inputData.userId, null, true)
    }
}