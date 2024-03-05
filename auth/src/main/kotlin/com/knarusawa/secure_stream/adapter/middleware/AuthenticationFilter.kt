package com.knarusawa.secure_stream.adapter.middleware

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.knarusawa.common.domain.flow.FlowId
import com.knarusawa.common.domain.socialLoginState.SocialLoginStateRepository
import com.knarusawa.common.domain.socialLoginState.State
import com.knarusawa.common.util.logger
import com.knarusawa.secure_stream.adapter.middleware.dto.SocialLoginAuthenticationToken
import com.knarusawa.secure_stream.adapter.middleware.dto.WebauthnAssertionAuthenticationToken
import com.knarusawa.secure_stream.application.service.completeWebauthnLogin.CompleteWebauthnLoginInputData
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.DelegatingSecurityContextRepository
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.security.web.context.SecurityContextRepository

class AuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    private val socialLoginStateRepository: SocialLoginStateRepository
) : UsernamePasswordAuthenticationFilter() {
    private var customSecurityContextRepository: SecurityContextRepository? = null

    init {
        this.customSecurityContextRepository = DelegatingSecurityContextRepository(
            RequestAttributeSecurityContextRepository(),
            HttpSessionSecurityContextRepository()
        )
        super.setSecurityContextRepository(customSecurityContextRepository)
    }

    companion object {
        private val log = logger()
    }

    override fun attemptAuthentication(
        request: HttpServletRequest, response: HttpServletResponse
    ): Authentication {
        log.info("Authentication Request. URI: [${request.requestURI}]")
        saveContext(request, response)

        if (request.requestURI == "/api/v1/login/webauthn") {
            val webauthnRequest = jacksonObjectMapper().readValue(
                request.inputStream,
                ApiV1WebauthnLoginPostRequest::class.java
            )

            log.info("Webauthn login challenge. flow_id: [${webauthnRequest.flowId}]")

            request.setAttribute("login_challenge", webauthnRequest.loginChallenge)
            val principal = FlowId.from(webauthnRequest.flowId)
            val credentials = CompleteWebauthnLoginInputData(
                flowId = webauthnRequest.flowId,
                credentialId = webauthnRequest.rawId,
                clientDataJSON = webauthnRequest.response.clientDataJSON,
                authenticatorData = webauthnRequest.response.authenticatorData,
                signature = webauthnRequest.response.signature,
                userHandle = webauthnRequest.response.userHandle
            )

            val authRequest: AbstractAuthenticationToken =
                WebauthnAssertionAuthenticationToken(
                    principal = principal,
                    credentials = credentials,
                )

            return this.authenticationManager.authenticate(authRequest)
        } else if (request.requestURI == "/api/v1/login/social_login") {
            val provider = request.getParameter("provider")
            val code = request.getParameter("code")
            val state = request.getParameter("state")
            val socialLoginState = socialLoginStateRepository.findByState(State.from(state))

            if (!socialLoginState.isValid()) {
                throw IllegalStateException("state is invalid")
            }

            request.setAttribute("login_challenge", socialLoginState.challenge)

            val principal = SocialLoginAuthenticationToken.SocialLoginPrincipal(
                provider = provider,
                state = state
            )

            val authRequest: AbstractAuthenticationToken =
                SocialLoginAuthenticationToken(
                    principal = principal,
                    credentials = code,
                )

            return this.authenticationManager.authenticate(authRequest)
        }

        val username = obtainUsername(request)
        val password = obtainPassword(request)
        val loginChallenge = request.getParameter("login_challenge")
        request.setAttribute("login_challenge", loginChallenge)

        val authRequest = UsernamePasswordAuthenticationToken(username, password)

        setDetails(request, authRequest)
        return this.authenticationManager.authenticate(authRequest)
    }

    // https://qiita.com/k-taichi/items/1787144fcad5d7bc8e41
    private fun saveContext(request: HttpServletRequest, response: HttpServletResponse?) {
        val securityContext = SecurityContextHolder.getContext()
        SecurityContextHolder.setContext(securityContext)
        customSecurityContextRepository!!.saveContext(securityContext, request, response)
    }

    data class ApiV1WebauthnLoginPostRequest(
        val loginChallenge: String,
        val flowId: String,
        val id: String,
        val rawId: String,
        val type: String,
        val response: Response
    ) {
        data class Response(
            val authenticatorData: String,
            val clientDataJSON: String,
            val signature: String,
            val userHandle: String?
        )
    }
}