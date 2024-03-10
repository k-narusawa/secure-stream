package com.knarusawa.secure_stream.config

import com.knarusawa.common.domain.socialLoginState.SocialLoginStateRepository
import com.knarusawa.secure_stream.adapter.middleware.AuthenticationFailureHandler
import com.knarusawa.secure_stream.adapter.middleware.AuthenticationFilter
import com.knarusawa.secure_stream.adapter.middleware.AuthenticationSuccessHandler
import com.knarusawa.secure_stream.adapter.middleware.AuthorizeFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
class SecurityConfig {
    @Autowired
    private lateinit var authenticationSuccessHandler: AuthenticationSuccessHandler

    @Autowired
    private lateinit var authenticationFailureHandler: AuthenticationFailureHandler

    @Autowired
    private lateinit var authenticationConfiguration: AuthenticationConfiguration

    @Autowired
    private lateinit var authorizeFilter: AuthorizeFilter

    @Autowired
    private lateinit var socialLoginStateRepository: SocialLoginStateRepository

    @Autowired
    private lateinit var environments: Environments

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors {
            it.configurationSource(this.corsConfigurationSource())
        }
        http.csrf {
            it.ignoringRequestMatchers("/api/v1/login/webauthn")
            it.csrfTokenRepository(CookieCsrfTokenRepository())
        }
        http.authorizeHttpRequests {
            it.requestMatchers(
                "/api/v1/csrf",
                "/api/v1/login",
                "/api/v1/login/webauthn",
                "/api/v1/login/webauthn/request",
                "/api/v1/login/social_login",
                "/api/v1/login/social_login/google",
                "/api/v1/logout",
            ).permitAll()
            it.anyRequest().authenticated()
        }
        http.addFilterBefore(authorizeFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun authenticationFilter(): UsernamePasswordAuthenticationFilter {
        val filter = AuthenticationFilter(authenticationManager(), socialLoginStateRepository)
        filter.setRequiresAuthenticationRequestMatcher {
            (it.method == "POST" && it.requestURI == "/api/v1/login") or
                (it.method == "POST" && it.requestURI == "/api/v1/login/webauthn") or
                (it.method == "GET" && it.requestURI == "/api/v1/login/social_login") or
                (it.method == "GET" && it.requestURI == "/api/v1/login/social_login/google")
        }
        filter.setAuthenticationManager(authenticationManager())
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler)
        filter.setAuthenticationFailureHandler(authenticationFailureHandler)
        return filter
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    private fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.addAllowedMethod(CorsConfiguration.ALL)
        config.addAllowedHeader(CorsConfiguration.ALL)
        config.allowCredentials = true

        val allowedOrigins = environments.allowedOrigin.split(",")

        allowedOrigins.forEach {
            config.addAllowedOrigin(it)
        }

        val corsSource = UrlBasedCorsConfigurationSource()
        corsSource.registerCorsConfiguration("/**", config)
        return corsSource
    }
}