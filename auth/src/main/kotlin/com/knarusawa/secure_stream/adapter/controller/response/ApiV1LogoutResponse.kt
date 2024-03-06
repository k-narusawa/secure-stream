package com.knarusawa.secure_stream.adapter.controller.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiV1LogoutResponse(
    @JsonProperty("redirect_to")
    val redirectTo: String
)
