package com.knarusawa.secure_stream.adapter.filter

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.knarusawa.secure_stream.domain.user.UserId
import com.knarusawa.secure_stream.domain.user.UserRepository
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
class AuthorizeFilter(
        private val userRepository: UserRepository
) : OncePerRequestFilter() {
    private val matcher = AntPathRequestMatcher("/api/v1/login")
    private val log = logger()
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (!matcher.matches(request)) {
            val bearerToken = request.getHeader("Authorization")

            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return
            }

            val decodedJWT = JWT.require(Algorithm.HMAC256("secret")).build().verify(bearerToken.substring(7))
            val userId = decodedJWT.subject

            val user = userRepository.findByUserId(userId = UserId.from(userId))
                    ?: throw IllegalStateException("Userが見つかりません")

            log.info("username: ${user.username}")
            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(user, null, ArrayList())
        }

        filterChain.doFilter(request, response)
    }
}