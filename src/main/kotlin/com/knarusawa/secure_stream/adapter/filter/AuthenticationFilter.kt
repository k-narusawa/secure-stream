package com.knarusawa.secure_stream.adapter.filter

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class AuthenticationFilter : UsernamePasswordAuthenticationFilter() {
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        return super.attemptAuthentication(request, response)
    }
}