package com.knarusawa.api.adapter.controller

import com.knarusawa.api.adapter.exception.UnauthorizedException
import com.knarusawa.common.domain.profile.ProfileRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/userinfo")
class UserInfoController(
        private val profileRepository: ProfileRepository
) {
    @GetMapping
    fun apiV1UserInfoGet(
            @AuthenticationPrincipal principal: OAuth2AuthenticatedPrincipal
    ): ApiV1UserinfoGetResponse {
        val userId = principal.getAttribute<String?>("sub")
                ?: throw UnauthorizedException()
        val profile = profileRepository.findByUserId(userId = userId)

        return ApiV1UserinfoGetResponse(
                userInfo = ApiV1UserinfoGetResponse.UserInfo(
                        userId = userId,
                        familyName = profile.familyName.value(),
                        givenName = profile.givenName.value(),
                        nickname = profile.nickname?.value(),
                        picture = profile.picture,
                )
        )
    }
}
