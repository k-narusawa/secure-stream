package com.knarusawa.secure_stream.adapter.controller

import com.knarusawa.secure_stream.adapter.controller.response.UserinfoResponse
import com.knarusawa.secure_stream.domain.user.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/userinfo")
class UserinfoController {
    @GetMapping
    fun apiV1UserinfoGet(): UserinfoResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = authentication.principal as? User
                ?: throw IllegalStateException("Principalが不正")

        return UserinfoResponse(
                userId = user.userId.value(),
                username = user.username.value()
        )
    }
}