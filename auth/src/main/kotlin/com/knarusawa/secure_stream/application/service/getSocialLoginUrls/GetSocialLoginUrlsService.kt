package com.knarusawa.secure_stream.application.service.getSocialLoginUrls

import com.knarusawa.common.domain.socialLoginState.SocialLoginState
import com.knarusawa.common.domain.socialLoginState.SocialLoginStateRepository
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
        return GetSocialLoginUrlsOutputData(
            githubUrl = generateGitHubUrl(inputData.loginChallenge),
            googleUrl = generateGoogleUrl(inputData.loginChallenge),
        )
    }

    private fun generateGitHubUrl(loginChallenge: String): String {
        val githubClient = clientRegistrationRepository.findByRegistrationId("github")
        val socialLoginState = SocialLoginState.of(challenge = loginChallenge)
        socialLoginStateRepository.save(socialLoginState)

        val authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
            .clientId(githubClient.clientId)
            .authorizationUri(githubClient.providerDetails.authorizationUri)
            .redirectUri(githubClient.redirectUri)
            .scopes(githubClient.scopes)
            .state(socialLoginState.state.value)
            .additionalParameters(Collections.emptyMap())
            .build()

        return authorizationRequest.authorizationRequestUri
    }

    private fun generateGoogleUrl(loginChallenge: String): String {
        val googleClient = clientRegistrationRepository.findByRegistrationId("google")
        val socialLoginState = SocialLoginState.of(challenge = loginChallenge)
        socialLoginStateRepository.save(socialLoginState)

        val authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
            .clientId(googleClient.clientId)
            .authorizationUri(googleClient.providerDetails.authorizationUri)
            .redirectUri(googleClient.redirectUri)
            .scopes(googleClient.scopes)
            .state(socialLoginState.state.value)
            .additionalParameters(Collections.emptyMap())
            .build()

        return authorizationRequest.authorizationRequestUri
    }
}