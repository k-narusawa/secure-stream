package com.knarusawa.api.application.service.getSocialLoginUrls

import com.knarusawa.common.domain.socialLoginState.SocialLoginState
import com.knarusawa.common.domain.socialLoginState.SocialLoginStateRepository
import com.knarusawa.common.domain.user.UserId
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class GetSocialLoginUrlsService(
    private val clientRegistrationRepository: ClientRegistrationRepository,
    private val socialLoginStateRepository: SocialLoginStateRepository
) {
    @Transactional
    fun exec(inputData: GetSocialLoginUrlsInputData): GetSocialLoginUrlsOutputData {
        val githubUrl = createGitHubAuthorizationUrl(userId = UserId.from(inputData.userId))
        val googleUrl = createGoogleAuthorizationUrl(userId = UserId.from(inputData.userId))

        return GetSocialLoginUrlsOutputData(
            github = githubUrl,
            google = googleUrl
        )
    }

    private fun createGitHubAuthorizationUrl(userId: UserId): String {
        val client = clientRegistrationRepository.findByRegistrationId("github")

        val socialLoginState = SocialLoginState.of(userId = userId)
        socialLoginStateRepository.save(socialLoginState)

        val authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
            .clientId(client.clientId)
            .authorizationUri(client.providerDetails.authorizationUri)
            .redirectUri(client.redirectUri)
            .scopes(client.scopes)
            .state(socialLoginState.state.value)
            .additionalParameters(Collections.emptyMap())
            .build()

        return authorizationRequest.authorizationRequestUri
    }

    private fun createGoogleAuthorizationUrl(userId: UserId): String {
        val client = clientRegistrationRepository.findByRegistrationId("google")

        val socialLoginState = SocialLoginState.of(userId = userId)
        socialLoginStateRepository.save(socialLoginState)

        val authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
            .clientId(client.clientId)
            .authorizationUri(client.providerDetails.authorizationUri)
            .redirectUri(client.redirectUri)
            .scopes(client.scopes)
            .state(socialLoginState.state.value)
            .additionalParameters(Collections.emptyMap())
            .build()

        return authorizationRequest.authorizationRequestUri
    }
}