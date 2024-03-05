package com.knarusawa.secure_stream.adapter.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sh.ory.hydra.api.OAuth2Api

@RestController
@RequestMapping("/api/v1/logout")
class LogoutController(
    private val oauth2Api: OAuth2Api
) {
    @PostMapping
    fun apiV1LogoutPost(request: HttpServletRequest, response: HttpServletResponse) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null) {
            SecurityContextLogoutHandler().logout(request, response, authentication)
        }
    }
}