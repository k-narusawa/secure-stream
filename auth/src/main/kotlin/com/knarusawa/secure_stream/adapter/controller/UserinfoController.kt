package com.knarusawa.secure_stream.adapter.controller

import com.knarusawa.common.domain.profile.ProfileRepository
import com.knarusawa.secure_stream.adapter.controller.response.UserinfoResponse
import com.knarusawa.secure_stream.domain.LoginUserDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/userinfo")
class UserinfoController(
    private val profileRepository: ProfileRepository
) {
    @GetMapping
    fun apiV1UserinfoGet(): UserinfoResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = authentication.principal as? LoginUserDetails
            ?: throw IllegalStateException("Principalが不正")

        profileRepository.findByUserId(userId = user.userId.toString())

        return UserinfoResponse(
            userId = user.userId.value(),
            username = user.username
        )
    }
}