package com.knarusawa.common.adapter.gateway.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleAccessTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("expires_in")
    val expiresIn: Long,
    @JsonProperty("refresh_token")
    val refreshToken: Long,
    @JsonProperty("scope")
    val scope: String?,
    @JsonProperty("token_type")
    val tokenType: String?,
    @JsonProperty("id_token")
    val idToken: Long,
)