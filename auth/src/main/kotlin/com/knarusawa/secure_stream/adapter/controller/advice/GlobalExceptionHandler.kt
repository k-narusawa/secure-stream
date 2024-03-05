package com.knarusawa.secure_stream.adapter.controller.advice

import com.knarusawa.common.util.logger
import com.knarusawa.secure_stream.adapter.controller.response.ErrorResponse
import com.knarusawa.secure_stream.adapter.exception.AuthenticationFailedException
import com.knarusawa.secure_stream.application.exception.UserNotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = logger()

    @ExceptionHandler(AuthenticationFailedException::class)
    fun handleAuthenticationFailedException(
        ex: AuthenticationFailedException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        log.warn("message: ${ex.message}, cause: ${ex.cause}, ex: $ex")
        return ResponseEntity(
            ErrorResponse.of(
                exception = ex,
                errorMessage = ex.message,
                logLevel = LogLevel.WARN
            ),
            HttpStatus.UNAUTHORIZED
        )
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFoundException(
        ex: UsernameNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        log.warn("message: ${ex.message}, cause: ${ex.cause}, ex: $ex")
        return ResponseEntity(
            ErrorResponse.of(
                exception = ex,
                errorMessage = ex.message ?: "ユーザーが見つかりませんでした",
                logLevel = LogLevel.WARN
            ),
            HttpStatus.UNAUTHORIZED
        )
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(
        ex: UserNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        log.warn("message: ${ex.message}, cause: ${ex.cause}, ex: $ex")
        return ResponseEntity(
            ErrorResponse.of(
                exception = ex,
                errorMessage = ex.message,
                logLevel = LogLevel.WARN
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        log.error("message: ${ex.message}, cause: ${ex.cause}, ex: $ex")
        log.warn(ex.stackTraceToString())
        return ResponseEntity(
            ErrorResponse.of(
                exception = ex,
                errorMessage = ex.message ?: "予期せぬエラーが発生しました",
                logLevel = LogLevel.ERROR
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}