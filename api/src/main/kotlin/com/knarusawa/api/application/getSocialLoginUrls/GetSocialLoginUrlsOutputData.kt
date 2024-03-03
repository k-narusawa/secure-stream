package com.knarusawa.api.application.getSocialLoginUrls

data class GetSocialLoginUrlsOutputData(
    val urls: List<Url>
) {
    data class Url(
        val provider: String,
        val url: String,
    )
}