package com.knarusawa.common.adapter.gateway.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginOauthAccessTokenBody(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("scope")
    val scope: String?,
    @JsonProperty("token_type")
    val tokenType: String?,
)