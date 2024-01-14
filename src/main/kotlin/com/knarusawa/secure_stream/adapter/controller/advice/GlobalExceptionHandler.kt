package com.knarusawa.secure_stream.adapter.controller.advice

import com.knarusawa.secure_stream.adapter.controller.response.ErrorResponse
import com.knarusawa.secure_stream.domain.exception.DomainException
import com.knarusawa.secure_stream.util.logger
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

    @ExceptionHandler(DomainException::class)
    fun handleDomainException(
            ex: DomainException,
            request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        log.warn("message: ${ex.message}, cause: ${ex.cause}, ex: $ex")
        return ResponseEntity(
                ErrorResponse.of(
                        exception = ex,
                        errorMessage = ex.message,
                        logLevel = LogLevel.WARN
                ),
                ex.status
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