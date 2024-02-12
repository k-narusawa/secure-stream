package com.knarusawa.secure_stream.adapter.controller.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiV1ConsentGetResponse(
        @JsonProperty("challenge")
        val challenge: String,
        @JsonProperty("scopes")
        val scopes: List<Scope>,
        @JsonProperty("csrf_token")
        val csrfToken: String,
) {
    data class Scope(
            @JsonProperty("name")
            val name: String,
            @JsonProperty("required")
            val required: Boolean,
            @JsonProperty("is_checked")
            val isChecked: Boolean
    )
}