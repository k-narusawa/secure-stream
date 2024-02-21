package com.knarusawa.secure_stream.config

import com.knarusawa.secure_stream.adapter.filter.AuthenticationFilter
import com.knarusawa.secure_stream.adapter.filter.AuthorizeFilter
import com.knarusawa.secure_stream.adapter.middleware.AuthenticationFailureHandler
import com.knarusawa.secure_stream.adapter.middleware.AuthenticationSuccessHandler
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
    private lateinit var environments: Environments

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors {
            it.configurationSource(this.corsConfigurationSource())
        }
        http.csrf {
            it.csrfTokenRepository(CookieCsrfTokenRepository())
        }
        http.authorizeHttpRequests {
            it.requestMatchers("/api/v1/csrf", "/api/v1/login").permitAll()
            it.anyRequest().authenticated()
        }
        http.addFilterBefore(authorizeFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun authenticationFilter(): UsernamePasswordAuthenticationFilter {
        val filter = AuthenticationFilter(authenticationManager())
        filter.setRequiresAuthenticationRequestMatcher {
            it.method == "POST" && it.requestURI == "/api/v1/login"
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