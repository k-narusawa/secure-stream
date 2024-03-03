package com.knarusawa.api.application.disconnectSocialLogin

import com.knarusawa.common.domain.socialLogin.Provider
import com.knarusawa.common.domain.socialLogin.SocialLoginRepository
import com.knarusawa.common.domain.user.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DisconnectSocialLoginService(
    private val socialLoginRepository: SocialLoginRepository
) {
    @Transactional
    fun exec(inputData: DisconnectSocialLoginInputData) {
        socialLoginRepository.deleteByUserIdAndProvider(
            userId = UserId.from(inputData.userId),
            provider = Provider.from(inputData.provider)
        )
    }
}