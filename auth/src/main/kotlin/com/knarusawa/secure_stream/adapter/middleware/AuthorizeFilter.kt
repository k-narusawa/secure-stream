package com.knarusawa.secure_stream.adapter.middleware

import com.knarusawa.common.domain.user.UserRepository
import com.knarusawa.common.util.logger
import com.knarusawa.secure_stream.domain.LoginUserDetails
import com.sun.jdi.request.InvalidRequestStateException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class AuthorizeFilter(
    private val userRepository: UserRepository
) : OncePerRequestFilter() {
    companion object {
        private val matchers = listOf(
            AntPathRequestMatcher("/api/v1/login"),
            AntPathRequestMatcher("/api/v1/login/webauthn"),
            AntPathRequestMatcher("/api/v1/login/webauthn/request"),
            AntPathRequestMatcher("/api/v1/login/social_login"),
            AntPathRequestMatcher("/api/v1/csrf"),
            AntPathRequestMatcher("/api/v1/logout"),
        )
        private val combinedMatcher = OrRequestMatcher(matchers)
        private val log = logger()
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        log.info("AuthorizeFilter")
        if (!combinedMatcher.matches(request)) {
            val user = request.session.getAttribute("user") as? LoginUserDetails
                ?: throw InvalidRequestStateException("想定外の認証エラー")

            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(user, null, ArrayList())
        }

        filterChain.doFilter(request, response)
    }
}