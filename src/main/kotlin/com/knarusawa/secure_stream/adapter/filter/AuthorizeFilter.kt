package com.knarusawa.secure_stream.adapter.filter

import com.knarusawa.secure_stream.util.logger
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class AuthorizeFilter : OncePerRequestFilter() {
    private val matcher = AntPathRequestMatcher("/api/v1/login")
    private val log = logger()
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        log.info("AuthorizeFilter")
        if (!matcher.matches(request)) {
            log.info("Session確認")
            // sessionからユーザー情報を取得してauthenticationに設定する
            request.session.attributeNames.asIterator().forEach {
                log.info(it)
            }
            val session = request.session
            val username = request.session.getAttribute("username") as? String
            log.info("username: ${username}")
            log.info("session: ${session}")
            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(username, null, ArrayList())
        }
        filterChain.doFilter(request, response)
    }
}