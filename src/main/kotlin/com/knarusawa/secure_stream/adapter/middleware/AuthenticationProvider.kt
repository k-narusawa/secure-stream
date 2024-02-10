package com.knarusawa.secure_stream.adapter.middleware

import com.knarusawa.secure_stream.adapter.exception.AuthenticationFailedException
import com.knarusawa.secure_stream.application.service.LoginUserDetailsService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AuthenticationProvider(
        private val loginUserDetailsService: LoginUserDetailsService,
        private val passwordEncoder: PasswordEncoder,
) : org.springframework.security.authentication.AuthenticationProvider {
    override fun supports(authentication: Class<*>?): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.principal as String
        val password = authentication.credentials as String

        val user = loginUserDetailsService.loadUserByUsername(username)

        if (!passwordEncoder.matches(password, user.password)) {
            throw AuthenticationFailedException(username = username, message = "認証に失敗しました")
        }

        return UsernamePasswordAuthenticationToken(user, password)
    }
}