package com.knarusawa.api.application.service.changePassword

import com.knarusawa.api.application.exception.UserNotFoundException
import com.knarusawa.common.domain.user.UserId
import com.knarusawa.common.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChangePasswordService(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun exec(inputData: ChangePasswordInputData) {
        val user = userRepository.findByUserId(UserId.from(inputData.userId))
            ?: throw UserNotFoundException()

        user.changePassword(inputData.password)
        userRepository.save(user)
    }
}