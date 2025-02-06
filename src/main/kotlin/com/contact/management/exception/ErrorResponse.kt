package com.contact.management.exception

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드는 JSON 응답에서 제외
data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String,
    val errors: Map<String, String>? = null // validation 에러 처리를 위한 필드 추가
)
