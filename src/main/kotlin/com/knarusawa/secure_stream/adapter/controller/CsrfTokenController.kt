package com.knarusawa.secure_stream.adapter.controller

import com.knarusawa.secure_stream.adapter.controller.response.ApiV1CsrfGetResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/csrf")
class CsrfTokenController {
    @GetMapping
    fun apiV1CsrfGet(
            csrfToken: CsrfToken,
            response: HttpServletResponse?
    ): ApiV1CsrfGetResponse {
        return ApiV1CsrfGetResponse(
                csrfToken = csrfToken.token.toString()
        )
    }
}