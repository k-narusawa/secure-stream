package com.knarusawa.api.application.getSocialLoginUrls

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
        val client = clientRegistrationRepository.findByRegistrationId("github")

        val socialLoginState = SocialLoginState.of(userId = UserId.from(inputData.userId))
        socialLoginStateRepository.save(socialLoginState)

        val authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
            .clientId(client.clientId)
            .authorizationUri(client.providerDetails.authorizationUri)
            .redirectUri(client.redirectUri)
            .scopes(client.scopes)
            .state(socialLoginState.state.value)
            .additionalParameters(Collections.emptyMap())
            .build()

        return GetSocialLoginUrlsOutputData(
            urls = listOf(
                GetSocialLoginUrlsOutputData.Url(provider = "github", url = authorizationRequest.authorizationRequestUri)
            )
        )
    }
}