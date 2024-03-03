package com.knarusawa.common.domain.socialLoginState

interface SocialLoginStateRepository {
    fun save(socialLoginState: SocialLoginState)
    fun findByState(state: State): SocialLoginState
    fun deleteByState(state: State)
}