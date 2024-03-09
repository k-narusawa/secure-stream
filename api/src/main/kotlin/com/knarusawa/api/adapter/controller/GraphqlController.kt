package com.knarusawa.api.adapter.controller

import com.knarusawa.api.adapter.controller.dto.*
import com.knarusawa.api.adapter.exception.UnauthorizedException
import com.knarusawa.api.application.service.changeProfile.ChangeProfileInputData
import com.knarusawa.api.application.service.changeProfile.ChangeProfileService
import com.knarusawa.api.application.service.query.ProfileDtoQueryService
import com.knarusawa.api.application.service.query.SocialLoginDtoQueryService
import com.knarusawa.api.application.service.query.UserDtoQueryService
import com.knarusawa.api.application.service.query.WebauthnCredentialsDtoQueryService
import com.knarusawa.common.domain.user.UserId
import com.webauthn4j.converter.AttestedCredentialDataConverter
import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.util.Base64UrlUtil
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
    private val socialLoginDtoQueryService: SocialLoginDtoQueryService,
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

        val webauthnCredentials =
            webauthnCredentialsDtoQueryService.findByUserId(userId = UserId.from(userInfo.userId))

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

    @SchemaMapping
    fun socialLogin(userInfo: UserInfo): SocialLogin {
        val socialLogin = socialLoginDtoQueryService.findByUserId(userId = userInfo.userId)

        return SocialLogin(
            google = socialLogin.google,
            github = socialLogin.github
        )
    }


    @SchemaMapping
    fun passkey(userInfo: UserInfo): List<Passkey> {
        val attestedCredentialDataConverter = AttestedCredentialDataConverter(ObjectConverter())

        val webauthnCredentials =
            webauthnCredentialsDtoQueryService.findByUserId(userId = UserId.from(userInfo.userId))
        return webauthnCredentials.map {
            Passkey(
                credentialId = it.credentialId,
                aaguid = attestedCredentialDataConverter.convert(Base64UrlUtil.decode(it.serializedAttestedCredentialData)).aaguid.value.toString()
            )
        }
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
