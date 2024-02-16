package com.knarusawa.secure_stream.application.service

import com.knarusawa.secure_stream.domain.LoginUserDetails
import com.knarusawa.secure_stream.domain.user.UserRepository
import com.knarusawa.secure_stream.domain.user.Username
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class LoginUserDetailsService(
        private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username = Username.of(username))
                ?: throw UsernameNotFoundException("対象のユーザーが見つかりませんでした。")

        return LoginUserDetails(user)
    }
}