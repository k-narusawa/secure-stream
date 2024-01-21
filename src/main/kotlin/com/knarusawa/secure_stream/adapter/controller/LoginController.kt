package com.knarusawa.secure_stream.adapter.controller

import com.knarusawa.secure_stream.adapter.controller.response.ApiV1LoginGetResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/login")
class LoginController {
    @GetMapping
    fun apiV1LoginGet(
            csrfToken: CsrfToken,
            response: HttpServletResponse?
    ): ApiV1LoginGetResponse {
        return ApiV1LoginGetResponse(
                csrfToken = csrfToken.token.toString()
        )
    }
}