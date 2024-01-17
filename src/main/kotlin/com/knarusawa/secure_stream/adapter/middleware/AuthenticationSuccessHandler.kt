package com.knarusawa.secure_stream.adapter.middleware

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.knarusawa.secure_stream.application.service.loginComplete.LoginCompleteInputData
import com.knarusawa.secure_stream.application.service.loginComplete.LoginCompleteService
import com.knarusawa.secure_stream.util.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class AuthenticationSuccessHandler(
        private val loginCompleteService: LoginCompleteService
) : org.springframework.security.web.authentication.AuthenticationSuccessHandler {
    private val log = logger()

    override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        log.info("onAuthenticationSuccess")

        val username = authentication?.principal as String
        loginCompleteService.execute(inputData = LoginCompleteInputData(
                username = username,
                remoteAddr = request?.remoteAddr ?: "",
                userAgent = request?.getHeader("User-Agent") ?: "",
                time = LocalDateTime.now()
        ))
        response?.status = HttpServletResponse.SC_OK
    }

    private fun createToken(userId: String): String {
        return JWT.create()
                .withSubject(userId)
                .withExpiresAt(Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .sign(Algorithm.HMAC256("__secret__"))
    }
}