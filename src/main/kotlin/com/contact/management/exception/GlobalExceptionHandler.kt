package com.contact.management.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CommonException::class)
    fun handleCommonException(e: CommonException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            e.exceptionCode.status.value(),
            e.exceptionCode.message,
        )
        return ResponseEntity(errorResponse, e.exceptionCode.status)
    }
}