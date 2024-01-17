package com.knarusawa.secure_stream.adapter.filter

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
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
            // headersのkeyを指定してトークンを取得します
            val xAuthToken = request.getHeader("X-AUTH-TOKEN")
            if (xAuthToken == null || !xAuthToken.startsWith("Bearer ")) {
                filterChain.doFilter(request, response)
                return
            }
            val decodedJWT: DecodedJWT = JWT.require(Algorithm.HMAC256("__secret__")).build().verify(xAuthToken.substring(7))
            val username: String = decodedJWT.getClaim("username").toString()
            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(username, null, ArrayList())
        }
        filterChain.doFilter(request, response)
    }
}