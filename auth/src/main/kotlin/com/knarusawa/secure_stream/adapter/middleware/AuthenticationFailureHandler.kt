package com.knarusawa.secure_stream.adapter.middleware

import com.knarusawa.common.util.logger
import com.knarusawa.secure_stream.adapter.exception.AuthenticationFailedException
import com.knarusawa.secure_stream.application.service.loginFailure.LoginFailureInputData
import com.knarusawa.secure_stream.application.service.loginFailure.LoginFailureService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AuthenticationFailureHandler(
    private val loginFailureService: LoginFailureService
) : org.springframework.security.web.authentication.AuthenticationFailureHandler {
    private val log = logger()
    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        val remoteAddr = request?.remoteAddr
        val userAgent = request?.getHeader("User-Agent")

        when (exception) {
            is AuthenticationFailedException -> {
                log.warn("認証失敗 username: ${exception.username}")
                loginFailureService.execute(
                    inputData = LoginFailureInputData(
                        username = exception.username,
                        remoteAddr = remoteAddr ?: "",
                        userAgent = userAgent ?: "",
                        time = LocalDateTime.now()
                    )
                )
            }

            else -> {
                log.error("想定外の認証失敗 ${exception?.message}")
                loginFailureService.execute(
                    inputData = LoginFailureInputData(
                        username = null,
                        remoteAddr = remoteAddr ?: "",
                        userAgent = userAgent ?: "",
                        time = LocalDateTime.now()
                    )
                )
            }
        }

        response?.status = HttpServletResponse.SC_UNAUTHORIZED
        response?.contentType = "application/json"
        response?.writer?.write("{\"message\":\"unauthorized\"}")
    }
}