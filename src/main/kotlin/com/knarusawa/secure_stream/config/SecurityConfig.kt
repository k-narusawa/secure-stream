package com.knarusawa.secure_stream.config

import com.knarusawa.secure_stream.adapter.filter.AuthorizeFilter
import com.knarusawa.secure_stream.adapter.middleware.AuthenticationFailureHandler
import com.knarusawa.secure_stream.adapter.middleware.AuthenticationSuccessHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
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

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors {
            it.configurationSource(this.corsConfigurationSource())
        }
        http.csrf {
            it.csrfTokenRepository(CookieCsrfTokenRepository())
        }
        http.authorizeHttpRequests {
            it.requestMatchers("/api/v1/login").permitAll()
            it.anyRequest().authenticated()
        }
        http.addFilterBefore(authorizeFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterAt(authenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun authenticationFilter(authenticationManager: AuthenticationManager): UsernamePasswordAuthenticationFilter {
        val filter = UsernamePasswordAuthenticationFilter()
        filter.setRequiresAuthenticationRequestMatcher {
            it.method == "POST" && it.requestURI == "/api/v1/login"
        }
        filter.setAuthenticationManager(authenticationManager)
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler)
        filter.setAuthenticationFailureHandler(authenticationFailureHandler)
        return filter
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    private fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.addAllowedMethod(CorsConfiguration.ALL)
        config.addAllowedHeader(CorsConfiguration.ALL)
        config.allowCredentials = true

        config.addAllowedOrigin("http://127.0.0.1:3000")
        config.addAllowedOrigin("http://localhost:3000")
        config.addAllowedOrigin("http://127.0.0.1:3001")
        config.addAllowedOrigin("http://localhost:3001")

        val corsSource = UrlBasedCorsConfigurationSource()
        corsSource.registerCorsConfiguration("/**", config)
        return corsSource
    }
}