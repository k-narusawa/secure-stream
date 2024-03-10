package com.knarusawa.common.adapter.gateway.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleProfile(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("email")
    val email: String? = null,
    @JsonProperty("name")
    val name: String? = null,
    @JsonProperty("given_name")
    val givenName: String? = null,
    @JsonProperty("family_name")
    val familyName: String? = null,
    @JsonProperty("picture")
    val picture: String? = null,
    @JsonProperty("locale")
    val locale: String? = null,
)