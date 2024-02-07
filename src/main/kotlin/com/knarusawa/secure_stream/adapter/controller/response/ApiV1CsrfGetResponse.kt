package com.knarusawa.secure_stream.adapter.controller.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiV1CsrfGetResponse(
        @JsonProperty("csrf_token")
        val csrfToken: String
)
