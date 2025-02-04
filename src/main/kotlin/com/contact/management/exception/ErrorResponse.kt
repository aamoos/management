package com.contact.management.exception

import java.time.LocalDateTime
import java.time.LocalDateTime.now

data class ErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String
) {
    constructor(status: Int, error: String) : this(now(), status, error)
}
