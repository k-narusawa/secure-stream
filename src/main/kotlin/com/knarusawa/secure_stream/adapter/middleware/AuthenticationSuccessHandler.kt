package com.knarusawa.secure_stream.adapter.middleware

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.knarusawa.secure_stream.application.service.loginComplete.LoginCompleteInputData
import com.knarusawa.secure_stream.application.service.loginComplete.LoginCompleteService
import com.knarusawa.secure_stream.domain.LoginUserDetails
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

        val user = authentication?.principal as LoginUserDetails
        loginCompleteService.execute(inputData = LoginCompleteInputData(
                username = user.username,
                remoteAddr = request?.remoteAddr ?: "",
                userAgent = request?.getHeader("User-Agent") ?: "",
                time = LocalDateTime.now()
        ))

        val jwt = createJwt(user)
        response?.status = HttpServletResponse.SC_OK
        response?.contentType = "application/son"
        response?.writer?.write("{\"access_token\":\"${jwt}\"}")
    }

    private fun createJwt(user: LoginUserDetails): String {
        val now = Date()
        val expiresAt = Date(now.time + (1000 * 60 * 60 * 8))
        return JWT.create()
                .withIssuer("secure_stream")
                .withSubject(user.userId.value())
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC256("secret"))
    }
}