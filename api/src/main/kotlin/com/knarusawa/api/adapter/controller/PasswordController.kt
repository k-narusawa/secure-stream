package com.knarusawa.api.adapter.controller

import com.knarusawa.api.adapter.exception.UnauthorizedException
import com.knarusawa.api.application.service.changePassword.ChangePasswordInputData
import com.knarusawa.api.application.service.changePassword.ChangePasswordService
import org.openapitools.api.PasswordApi
import org.openapitools.model.ChangePassword
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PasswordController(
    private val changePasswordService: ChangePasswordService
) : PasswordApi {
    override fun changePassword(@RequestBody changePassword: ChangePassword): ResponseEntity<Unit> {
        val principal =
            SecurityContextHolder.getContext().authentication.principal as? OAuth2AuthenticatedPrincipal

        val userId = principal?.getAttribute<String?>("sub")
            ?: throw UnauthorizedException()

        val inputData = ChangePasswordInputData(
            userId = userId,
            password = changePassword.password
        )
        changePasswordService.exec(inputData = inputData)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}