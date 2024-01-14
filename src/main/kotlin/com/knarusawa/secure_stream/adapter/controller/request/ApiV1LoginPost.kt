package com.knarusawa.secure_stream.adapter.controller.request

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiV1LoginPost(
        @JsonProperty("username")
        val username: String,
        @JsonProperty("password")
        val password: String
)
