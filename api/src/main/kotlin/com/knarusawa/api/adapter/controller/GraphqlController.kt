package com.knarusawa.api.adapter.controller

import com.knarusawa.api.adapter.controller.dto.Profile
import com.knarusawa.api.adapter.controller.dto.User
import com.knarusawa.api.adapter.controller.dto.UserInfo
import com.knarusawa.api.adapter.exception.UnauthorizedException
import com.knarusawa.api.application.changeProfile.ChangeProfileInputData
import com.knarusawa.api.application.changeProfile.ChangeProfileService
import com.knarusawa.api.application.query.ProfileDtoQueryService
import com.knarusawa.api.application.query.UserDtoQueryService
import com.knarusawa.api.application.query.WebauthnCredentialsDtoQueryService
import com.knarusawa.common.domain.user.UserId
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.stereotype.Controller

@Controller
class GraphqlController(
        private val userDtoQueryService: UserDtoQueryService,
        private val profileDtoQueryService: ProfileDtoQueryService,
        private val webauthnCredentialsDtoQueryService: WebauthnCredentialsDtoQueryService,
        private val changeProfileService: ChangeProfileService
) {
    @QueryMapping
    fun userInfo(
            @AuthenticationPrincipal principal: OAuth2AuthenticatedPrincipal
    ): UserInfo {
        val userId = principal.getAttribute<String?>("sub")
                ?: throw UnauthorizedException()

        return UserInfo(
                userId = userId,
                user = null,
                profile = null
        )
    }

    @SchemaMapping
    fun user(userInfo: UserInfo): User {
        val user = userDtoQueryService.findByUserId(userInfo.userId)
                ?: throw UnauthorizedException()

        val webauthnCredentials = webauthnCredentialsDtoQueryService.findByUserId(userId = UserId.from(userInfo.userId))
        
        return User(
                username = user.username,
                isAccountLock = user.isAccountLock,
                mfa = false,
                passkey = webauthnCredentials.isNotEmpty()
        )
    }

    @SchemaMapping
    fun profile(userInfo: UserInfo): Profile {
        val profile = profileDtoQueryService.findByUserId(userInfo.userId)

        return Profile(
                familyName = profile.familyName,
                givenName = profile.givenName,
                nickname = profile.nickname,
                picture = profile.picture
        )
    }

    @MutationMapping
    fun changeProfile(
            @Argument familyName: String,
            @Argument givenName: String,
            @Argument nickname: String?,
            @Argument picture: String?,
            @AuthenticationPrincipal principal: OAuth2AuthenticatedPrincipal
    ): Profile {
        val userId = principal.getAttribute<String?>("sub")
                ?: throw UnauthorizedException()

        val inputData = ChangeProfileInputData(
                userId = userId,
                familyName = familyName,
                givenName = givenName,
                nickname = nickname,
                picture = picture
        )

        changeProfileService.exec(inputData)

        val profile = profileDtoQueryService.findByUserId(userId)
        return Profile(
                familyName = profile.familyName,
                givenName = profile.givenName,
                nickname = profile.nickname,
                picture = profile.picture
        )
    }
}
