package com.knarusawa.api.adapter.controller

import com.knarusawa.api.adapter.exception.UnauthorizedException
import com.knarusawa.api.application.connectSocialLogin.ConnectSocialLoginInputData
import com.knarusawa.api.application.connectSocialLogin.ConnectSocialLoginService
import com.knarusawa.api.application.disconnectSocialLogin.DisconnectSocialLoginInputData
import com.knarusawa.api.application.disconnectSocialLogin.DisconnectSocialLoginService
import com.knarusawa.api.application.getSocialLoginUrls.GetSocialLoginUrlsInputData
import com.knarusawa.api.application.getSocialLoginUrls.GetSocialLoginUrlsService
import com.knarusawa.common.util.logger
import org.jetbrains.annotations.NotNull
import org.openapitools.api.SocialLoginApi
import org.openapitools.model.SocialLoginUrls
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
class SocialLoginController(
    private val getSocialLoginUrlsService: GetSocialLoginUrlsService,
    private val connectSocialLoginService: ConnectSocialLoginService,
    private val disconnectSocialLoginService: DisconnectSocialLoginService,
) : SocialLoginApi {
    companion object {
        private val log = logger()
    }

    override fun getSocialLoginUrl(): ResponseEntity<SocialLoginUrls> {
        val principal =
            SecurityContextHolder.getContext().authentication.principal as? OAuth2AuthenticatedPrincipal
        val userId = principal?.getAttribute<String?>("sub")
            ?: throw UnauthorizedException()

        val outputData =
            getSocialLoginUrlsService.exec(GetSocialLoginUrlsInputData(userId = userId))

        return ResponseEntity.ok(
            SocialLoginUrls(
                google = outputData.google,
                github = outputData.github
            )
        )
    }

    override fun requestAuthorizationCode(
        @PathVariable("provider") provider: String,
        @NotNull @RequestParam(value = "code", required = true) code: String,
        @RequestParam(value = "state", required = false) state: String?
    ): ResponseEntity<Unit> {
        if (state == null) throw RuntimeException("stateが存在しません")

        val resHeaders = HttpHeaders()
        try {
            val inputData = ConnectSocialLoginInputData(
                userId = "userId",
                provider = provider,
                code = code,
                state = state
            )
            connectSocialLoginService.exec(inputData = inputData)

            resHeaders.add("Location", "http://localhost:3000/account/social_login/complete")
            return ResponseEntity(resHeaders, HttpStatus.FOUND)
        } catch (ex: Exception) {
            ex.printStackTrace()

            log.error("${ex.message}")

            resHeaders.add("Location", "http://localhost:3000/error")
            return ResponseEntity(resHeaders, HttpStatus.FOUND)
        }
    }

    override fun deleteSocialLogin(
        @PathVariable("provider") provider: String
    ): ResponseEntity<Unit> {
        val principal =
            SecurityContextHolder.getContext().authentication.principal as? OAuth2AuthenticatedPrincipal

        val userId = principal?.getAttribute<String?>("sub")
            ?: throw UnauthorizedException()

        val inputData = DisconnectSocialLoginInputData(
            userId = userId,
            provider = provider
        )
        disconnectSocialLoginService.exec(inputData)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    override fun requestAuthorizationCodeForLogin(
        @PathVariable("provider") provider: kotlin.String,
        @NotNull @RequestParam(value = "code", required = true) code: String,
        @RequestParam(value = "state", required = false) state: String?,
        @RequestParam(value = "login_challenge", required = false) loginChallenge: String?,
    ): ResponseEntity<Unit> {
        val resHeaders = HttpHeaders()
        val url = UriComponentsBuilder
            .fromUriString("http://localhost:8080")
            .path("/api/v1/login/social_login")
            .query("provider=${provider}")
            .query("code=${code}")
            .query("state=${state}")
            .query("login_challenge=${loginChallenge}")
            .build().toString()
        resHeaders.add("Location", url)
        return ResponseEntity(resHeaders, HttpStatus.FOUND)
    }
}