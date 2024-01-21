package com.knarusawa.secure_stream.adapter.filter

import com.knarusawa.secure_stream.domain.LoginUserDetails
import com.knarusawa.secure_stream.domain.user.UserRepository
import com.knarusawa.secure_stream.util.logger
import com.sun.jdi.request.InvalidRequestStateException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class AuthorizeFilter(
        private val userRepository: UserRepository
) : OncePerRequestFilter() {
    private val matcher = AntPathRequestMatcher("/api/v1/login")
    private val log = logger()
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (!matcher.matches(request)) {
            val user = request.session.getAttribute("user") as? LoginUserDetails
                    ?: throw InvalidRequestStateException("想定外の認証エラー")

            log.info("user_id: ${user.userId}")
            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(user, null, ArrayList())
        }

        filterChain.doFilter(request, response)
    }
}