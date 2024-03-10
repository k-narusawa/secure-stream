package com.knarusawa.secure_stream.adapter.controller.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiV1LoginGetResponse(
    @JsonProperty("csrf_token")
    val csrfToken: String,
    @JsonProperty("redirect_to")
    val redirectTo: String?,
    @JsonProperty("google_login_url")
    val googleLoginUrl: String?,
    @JsonProperty("github_login_url")
    val githubLoginUrl: String?,
)
