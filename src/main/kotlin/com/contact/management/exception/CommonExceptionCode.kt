package com.contact.management.exception

import org.springframework.http.HttpStatus

enum class CommonExceptionCode(
    val status: HttpStatus,
    val message: String,
) {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다."),
}
