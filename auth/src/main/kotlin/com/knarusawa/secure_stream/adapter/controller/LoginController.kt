package com.knarusawa.secure_stream.adapter.controller

import com.knarusawa.secure_stream.adapter.controller.response.ApiV1LoginGetResponse
import com.knarusawa.secure_stream.application.service.getSocialLoginUrls.GetSocialLoginUrlsInputData
import com.knarusawa.secure_stream.application.service.getSocialLoginUrls.GetSocialLoginUrlsService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sh.ory.hydra.api.OAuth2Api


@RestController
@RequestMapping("/api/v1/login")
class LoginController(
    private val oAuth2Api: OAuth2Api,
    private val getSocialLoginUrlsService: GetSocialLoginUrlsService,
) {
    @GetMapping
    fun apiV1LoginGet(
        @RequestParam("login_challenge") loginChallenge: String,
        csrfToken: CsrfToken,
        response: HttpServletResponse?
    ): ApiV1LoginGetResponse {
        val outputData =
            getSocialLoginUrlsService.exec(inputData = GetSocialLoginUrlsInputData(loginChallenge))

        // 以下のバグが修正されるまでスキップはできない
        // https://github.com/ory/hydra-client-java/issues/19
        return ApiV1LoginGetResponse(
            csrfToken = csrfToken.token.toString(),
            redirectTo = null,
            githubLoginUrl = outputData.githubUrl,
        )

//        if (loginChallenge == null) {
//            return ApiV1LoginGetResponse(
//                    csrfToken = csrfToken.token.toString(),
//                    redirectTo = null
//            )
//        }
//
//        val requestResponse = oAuth2Api.getOAuth2LoginRequest(loginChallenge)
//
//        if (!requestResponse.skip) {
//            return ApiV1LoginGetResponse(
//                    csrfToken = csrfToken.token.toString(),
//                    redirectTo = null
//            )
//        }
//
//
//        val authentication = SecurityContextHolder.getContext().authentication
//        val user = authentication.principal as? LoginUserDetails
//                ?: return ApiV1LoginGetResponse(
//                        csrfToken = csrfToken.token.toString(),
//                        redirectTo = null
//                )
//
//        val req = AcceptOAuth2LoginRequest().subject(user.username)
//        val acceptResponse = oAuth2Api.acceptOAuth2LoginRequest(loginChallenge, req)
//
//        return ApiV1LoginGetResponse(
//                csrfToken = csrfToken.token.toString(),
//                redirectTo = acceptResponse.redirectTo
//        )
    }
}