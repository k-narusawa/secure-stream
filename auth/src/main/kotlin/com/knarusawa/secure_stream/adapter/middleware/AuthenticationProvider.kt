package com.knarusawa.secure_stream.adapter.middleware

import com.knarusawa.common.domain.user.UserRepository
import com.knarusawa.secure_stream.adapter.exception.AuthenticationFailedException
import com.knarusawa.secure_stream.adapter.middleware.dto.WebauthnAssertionAuthenticationToken
import com.knarusawa.secure_stream.application.service.LoginUserDetailsService
import com.knarusawa.secure_stream.application.service.completeWebauthnLogin.CompleteWebauthnLoginService
import com.knarusawa.secure_stream.domain.LoginUserDetails
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AuthenticationProvider(
        private val userRepository: UserRepository,
        private val completeWebauthnLoginService: CompleteWebauthnLoginService,
        private val loginUserDetailsService: LoginUserDetailsService,
        private val passwordEncoder: PasswordEncoder,
) : org.springframework.security.authentication.AuthenticationProvider {
    override fun supports(authentication: Class<*>?): Boolean {
        return AbstractAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    override fun authenticate(authentication: Authentication): Authentication {
        if (authentication is WebauthnAssertionAuthenticationToken) {
            val outputData = completeWebauthnLoginService.exec(authentication.credentials)

            val user = userRepository.findByUserId(userId = outputData.userId)
                    ?: throw AuthenticationFailedException("ユーザーの識別に失敗しました")

            val loginUser = LoginUserDetails(user)

            return UsernamePasswordAuthenticationToken(loginUser, null)
        }
        
        val username = authentication.principal as String
        val password = authentication.credentials as String

        val user = loginUserDetailsService.loadUserByUsername(username)

        if (!passwordEncoder.matches(password, user.password)) {
            throw AuthenticationFailedException(username = username, message = "認証に失敗しました")
        }

        return UsernamePasswordAuthenticationToken(user, password)
    }
}