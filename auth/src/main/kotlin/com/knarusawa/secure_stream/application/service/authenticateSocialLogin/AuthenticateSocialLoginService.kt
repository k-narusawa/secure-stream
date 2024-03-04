package com.knarusawa.secure_stream.application.service.authenticateSocialLogin

import com.knarusawa.common.adapter.gateway.api.GitHubApiWebClient
import com.knarusawa.common.adapter.gateway.api.GitHubWebClient
import com.knarusawa.common.domain.socialLogin.Provider
import com.knarusawa.common.domain.socialLogin.SocialLoginRepository
import com.knarusawa.secure_stream.application.exception.UserNotFoundException
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthenticateSocialLoginService(
    private val clientRegistrationRepository: ClientRegistrationRepository,
    private val githubWebClient: GitHubWebClient,
    private val gitHubApiWebClient: GitHubApiWebClient,
    private val socialLoginRepository: SocialLoginRepository,
) {
    @Transactional
    fun exec(inputData: AuthenticateSocialLoginInputData): AuthenticateSocialLoginOutputData {
        return when (inputData.provider) {
            "github" -> checkGitHub(inputData.code)
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
        val socialLogin = socialLoginRepository.findBySubAndProvider(sub = githubUser.id.toString(), provider = Provider.GITHUB)
            ?: throw UserNotFoundException("SocialLoginに紐づくユーザーが見つかりませんでした")

        return AuthenticateSocialLoginOutputData(
            userId = socialLogin.userId
        )
    }
}