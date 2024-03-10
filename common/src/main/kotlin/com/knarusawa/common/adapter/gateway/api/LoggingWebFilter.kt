package com.knarusawa.common.adapter.gateway.api

import com.knarusawa.common.util.logger
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain

@Component
class LoggingWebFilter : WebFilter {
    companion object {
        private val log = logger()
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain) = chain.filter(LoggingWebExchange(exchange))
}