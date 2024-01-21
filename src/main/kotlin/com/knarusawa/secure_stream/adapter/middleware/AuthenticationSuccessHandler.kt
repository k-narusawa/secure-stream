package com.knarusawa.secure_stream.adapter.middleware

import com.knarusawa.secure_stream.application.service.loginComplete.LoginCompleteInputData
import com.knarusawa.secure_stream.application.service.loginComplete.LoginCompleteService
import com.knarusawa.secure_stream.domain.LoginUserDetails
import com.knarusawa.secure_stream.util.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.LocalDateTime


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

        val session = request?.session
        session?.setAttribute("user", user)
        response?.status = HttpServletResponse.SC_OK
    }
}