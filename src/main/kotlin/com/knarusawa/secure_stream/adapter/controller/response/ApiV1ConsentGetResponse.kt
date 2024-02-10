package com.knarusawa.secure_stream.adapter.controller.response

data class ApiV1ConsentGetResponse(
        val challenge: String,
        val scopes: List<String>
)