package com.knarusawa.api.adapter.controller.dto

data class ApiV1WebauthnPostRequest(
        val flowId: String,
        val id: String,
        val rawId: String,
        val type: String,
        val response: Response
) {
    data class Response(
            val attestationObject: String,
            val clientDataJSON: String
    )
}
