package com.knarusawa.api.adapter.controller

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiV1UserinfoGetResponse(
        @JsonProperty("user_info")
        val userInfo: UserInfo
) {
    data class UserInfo(
            @JsonProperty("user_id")
            val userId: String,
            @JsonProperty("family_name")
            val familyName: String,
            @JsonProperty("given_name")
            val givenName: String,
            @JsonProperty("nickname")
            val nickname: String?,
            @JsonProperty("picture")
            val picture: String?
    )
}