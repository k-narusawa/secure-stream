package com.knarusawa.secure_stream.adapter.controller

import com.knarusawa.secure_stream.adapter.controller.response.ApiV1LogoutGetResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/logout")
class LogoutController {
    @GetMapping
    fun apiV1LogoutGet(
        csrfToken: CsrfToken,
        response: HttpServletResponse?
    ): ApiV1LogoutGetResponse {
        return ApiV1LogoutGetResponse(
            csrfToken = csrfToken.token.toString()
        )
    }

    @PostMapping
    fun apiV1LogoutPost(request: HttpServletRequest, response: HttpServletResponse) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null) {
            SecurityContextLogoutHandler().logout(request, response, authentication)
        }
    }
}