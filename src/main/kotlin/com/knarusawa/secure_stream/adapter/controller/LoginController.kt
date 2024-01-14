package com.knarusawa.secure_stream.adapter.controller

import com.knarusawa.secure_stream.adapter.controller.request.ApiV1LoginPost
import com.knarusawa.secure_stream.util.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api/v1/login")
class LoginController(
        private val daoAuthenticationProvider: DaoAuthenticationProvider,
) {
    private val log = logger()

    @PostMapping
    fun apiV1LoginPost(
            @RequestBody apiV1LoginPost: ApiV1LoginPost
    ): ResponseEntity<String> {
        log.info("ログイン要求")
        val authToken = UsernamePasswordAuthenticationToken(
                apiV1LoginPost.username,
                apiV1LoginPost.password
        )
        daoAuthenticationProvider.authenticate(authToken)
        return ResponseEntity(HttpStatus.OK)
    }
}