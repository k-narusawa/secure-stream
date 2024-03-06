package com.knarusawa.secure_stream.adapter.middleware

import com.knarusawa.common.util.logger
import com.knarusawa.secure_stream.application.service.loginComplete.LoginCompleteInputData
import com.knarusawa.secure_stream.application.service.loginComplete.LoginCompleteService
import com.knarusawa.secure_stream.config.Environments
import com.knarusawa.secure_stream.domain.LoginUserDetails
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import sh.ory.hydra.api.OAuth2Api
import sh.ory.hydra.model.AcceptOAuth2LoginRequest
import java.time.LocalDateTime


@Component
class AuthenticationSuccessHandler(
    private val loginCompleteService: LoginCompleteService,
    private val environments: Environments,
    private val oAuth2Api: OAuth2Api,
) : org.springframework.security.web.authentication.AuthenticationSuccessHandler {
    companion object {
        private val log = logger()
    }

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val user = authentication?.principal as LoginUserDetails
        log.info("認証成功 user_id: [${user.userId.value()}]")

        val session = request?.session
        session?.setAttribute("user", user)

        val loginChallenge = request?.getAttribute("login_challenge") as? String
        request?.removeAttribute("login_challenge")
        log.info("login_challenge: $loginChallenge")

        if (loginChallenge != null) {
            loginCompleteService.execute(
                inputData = LoginCompleteInputData(
                    username = user.username,
                    remoteAddr = request?.remoteAddr ?: "",
                    userAgent = request?.getHeader("User-Agent") ?: "",
                    time = LocalDateTime.now()
                )
            )

            val req = AcceptOAuth2LoginRequest()
                .subject(user.userId.value())
                .remember(true)
                .rememberFor(3600)
            val res = oAuth2Api
                .acceptOAuth2LoginRequest(loginChallenge, req)

            if (request.requestURI == "/api/v1/login/social_login") {
                response?.sendRedirect(res.redirectTo)
                return
            }

            response?.setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            response?.writer?.println(
                "{ \"redirect_to\": \"${res.redirectTo}\"}"
            )

            return
        }

        val errorPage = UriComponentsBuilder.fromUriString(environments.spFrontUrl)
            .path("/error")
            .build()
            .toUriString()

        response?.sendRedirect(errorPage)
    }
}