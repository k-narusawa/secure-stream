package com.knarusawa.secure_stream.adapter.controller

import com.knarusawa.secure_stream.adapter.controller.response.UserinfoResponse
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
        val username: String = authentication.principal.toString()

        return UserinfoResponse(
                userId = "test",
                username = username
        )
    }
}