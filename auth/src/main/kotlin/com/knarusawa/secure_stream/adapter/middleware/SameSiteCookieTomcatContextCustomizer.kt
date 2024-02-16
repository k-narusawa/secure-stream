package com.knarusawa.secure_stream.adapter.middleware

import org.apache.catalina.Context
import org.apache.tomcat.util.http.Rfc6265CookieProcessor

import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer
import org.springframework.stereotype.Component


@Component
class SameSiteCookieTomcatContextCustomizer : TomcatContextCustomizer {
    override fun customize(context: Context) {
        val cookieProcessor = Rfc6265CookieProcessor()
        cookieProcessor.setSameSiteCookies("Lax")
        context.cookieProcessor = cookieProcessor
    }
}