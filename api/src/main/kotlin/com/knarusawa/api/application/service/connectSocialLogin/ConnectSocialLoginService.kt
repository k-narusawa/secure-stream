package com.knarusawa.api.application.service.connectSocialLogin

import com.knarusawa.api.config.Environments
import com.knarusawa.common.adapter.gateway.api.GitHubApiWebClient
import com.knarusawa.common.adapter.gateway.api.GitHubWebClient
import com.knarusawa.common.adapter.gateway.api.GoogleApiClient
import com.knarusawa.common.domain.socialLogin.Provider
import com.knarusawa.common.domain.socialLogin.SocialLogin
import com.knarusawa.common.domain.socialLogin.SocialLoginRepository
import com.knarusawa.common.domain.socialLoginState.SocialLoginStateRepository
import com.knarusawa.common.domain.socialLoginState.State
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ConnectSocialLoginService(
    private val clientRegistrationRepository: ClientRegistrationRepository,
    private val githubWebClient: GitHubWebClient,
    private val gitHubApiWebClient: GitHubApiWebClient,
    private val googleApiClient: GoogleApiClient,
    private val socialLoginRepository: SocialLoginRepository,
    private val socialLoginStateRepository: SocialLoginStateRepository,
    private val environments: Environments
) {
    @Transactional
    fun exec(inputData: ConnectSocialLoginInputData) {
        when (inputData.provider) {
            "github" -> connectGithub(
                state = State.from(value = inputData.state),
                code = inputData.code
            )

            "google" -> connectGoogle(
                state = State.from(value = inputData.state),
                code = inputData.code
            )

            else -> throw RuntimeException("想定外のプロバイダです")
        }
    }

    private fun connectGithub(state: State, code: String) {
        val client = clientRegistrationRepository.findByRegistrationId("github")

        val res = githubWebClient.loginOauthAccessToken(
            clientId = client.clientId,
            clientSecret = client.clientSecret,
            code = code,
            redirectUri = client.redirectUri
        )

        val socialLoginState = socialLoginStateRepository.findByState(state = state)
        socialLoginState.isValid()
        socialLoginStateRepository.deleteByState(state = state)

        val githubUser = gitHubApiWebClient.user(accessToken = res.accessToken)
        val socialLogin = SocialLogin.of(
            userId = socialLoginState.userId!!,
            provider = Provider.GITHUB,
            sub = githubUser.id.toString()
        )

        socialLoginRepository.save(socialLogin)
    }

    private fun connectGoogle(state: State, code: String) {
        val client = clientRegistrationRepository.findByRegistrationId("google")

        val res = googleApiClient.loginOauthAccessToken(
            clientId = client.clientId,
            clientSecret = client.clientSecret,
            code = code,
            redirectUri = client.redirectUri,
            scope = client.scopes.first()
        )

        println(res.accessToken)

        val socialLoginState = socialLoginStateRepository.findByState(state = state)
        socialLoginState.isValid()
        socialLoginStateRepository.deleteByState(state = state)

        val googleUser = googleApiClient.profile(accessToken = res.accessToken)
        val socialLogin = SocialLogin.of(
            userId = socialLoginState.userId!!,
            provider = Provider.GOOGLE,
            sub = googleUser.id
        )

        socialLoginRepository.save(socialLogin)
    }
}