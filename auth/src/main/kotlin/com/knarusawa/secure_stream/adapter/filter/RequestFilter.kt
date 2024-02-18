package com.knarusawa.secure_stream.adapter.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class RequestFilter : OncePerRequestFilter() {
    companion object {
        val excludeUrlRegexList = listOf(
                Regex(".*webjars.*"),
                Regex(".*csv.*"),
                Regex(".*\\.png"),
        )
    }

    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
    ) {
        val requestId = UUID.randomUUID().toString()
        MDC.put("requestId", requestId)

        logger.info("リクエスト受信 メソッド:[${request.method}], URI:[${request.requestURI}]")

        val start = System.currentTimeMillis()
        try {
            filterChain.doFilter(request, response)
        } finally {
            val end = System.currentTimeMillis()
            logger.info("レスポンス返却 メソッド:[${request.method}], URI:[${request.requestURI}], ステータス:[${response.status}], レスポンスタイム:[${end - start}ms]")
            MDC.clear()
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return excludeUrlRegexList.any { path.matches(it) }
    }

}