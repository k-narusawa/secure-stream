package com.knarusawa.api.adapter.controller.advice

import com.knarusawa.api.application.exception.ApplicationException
import com.knarusawa.common.util.logger
import jakarta.servlet.http.HttpServletRequest
import org.openapitools.model.Error
import org.openapitools.model.ErrorError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    companion object {
        private val log = logger()
    }

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(
        ex: ApplicationException,
        request: HttpServletRequest
    ): ResponseEntity<Error> {
        log.error("message: ${ex.message}, cause: ${ex.cause}, ex: $ex")
        log.warn(ex.stackTraceToString())
        return ResponseEntity(
            Error(
                error = ErrorError(
                    requestId = "",
                    code = ex.code.code,
                    message = ex.message
                )
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<Error> {
        log.error("message: ${ex.message}, cause: ${ex.cause}, ex: $ex")
        log.warn(ex.stackTraceToString())
        return ResponseEntity(
            Error(
                error = ErrorError(
                    requestId = "",
                    code = ErrorError.Code.ID500_E001,
                    message = ex.message ?: "Internal Server Error"
                )
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}