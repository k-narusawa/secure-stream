package com.knarusawa.secure_stream.application.service.authenticateSocialLogin

import com.knarusawa.common.adapter.gateway.api.GitHubApiWebClient
import com.knarusawa.common.adapter.gateway.api.GitHubWebClient
import com.knarusawa.common.adapter.gateway.api.GoogleApiClient
import com.knarusawa.common.domain.socialLogin.Provider
import com.knarusawa.common.domain.socialLogin.SocialLoginRepository
import com.knarusawa.common.domain.socialLoginState.SocialLoginStateRepository
import com.knarusawa.common.domain.socialLoginState.State
import com.knarusawa.secure_stream.application.exception.UserNotFoundException
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthenticateSocialLoginService(
    private val clientRegistrationRepository: ClientRegistrationRepository,
    private val githubWebClient: GitHubWebClient,
    private val gitHubApiWebClient: GitHubApiWebClient,
    private val googleApiClient: GoogleApiClient,
    private val socialLoginRepository: SocialLoginRepository,
    private val socialLoginStateRepository: SocialLoginStateRepository
) {
    @Transactional
    fun exec(inputData: AuthenticateSocialLoginInputData): AuthenticateSocialLoginOutputData {
        socialLoginStateRepository.deleteByState(State.from(inputData.state))
        return when (inputData.provider) {
            "github" -> checkGitHub(inputData.code)
            "google" -> checkGoogle(inputData.code)
            else -> throw IllegalStateException("想定外のプロバイダです")
        }
    }

    fun checkGitHub(code: String): AuthenticateSocialLoginOutputData {
        val client = clientRegistrationRepository.findByRegistrationId("github")
        val res = githubWebClient.loginOauthAccessToken(
            clientId = client.clientId,
            clientSecret = client.clientSecret,
            code = code,
            redirectUri = "http://localhost:8080"
        )

        val githubUser = gitHubApiWebClient.user(res.accessToken)
        val socialLogin = socialLoginRepository.findBySubAndProvider(
            sub = githubUser.id.toString(),
            provider = Provider.GITHUB
        )
            ?: throw UserNotFoundException("SocialLoginに紐づくユーザーが見つかりませんでした")

        return AuthenticateSocialLoginOutputData(
            userId = socialLogin.userId
        )
    }

    fun checkGoogle(code: String): AuthenticateSocialLoginOutputData {
        val client = clientRegistrationRepository.findByRegistrationId("google")
        val res = googleApiClient.loginOauthAccessToken(
            clientId = client.clientId,
            clientSecret = client.clientSecret,
            code = code,
            redirectUri = "http://localhost:8081/api/v1/users/social_login/login/code/google",
            scope = client.scopes.first()
        )

        val googleUser = googleApiClient.profile(res.accessToken)
        val socialLogin = socialLoginRepository.findBySubAndProvider(
            sub = googleUser.id,
            provider = Provider.GOOGLE
        )
            ?: throw UserNotFoundException("SocialLoginに紐づくユーザーが見つかりませんでした")

        return AuthenticateSocialLoginOutputData(
            userId = socialLogin.userId
        )
    }
}