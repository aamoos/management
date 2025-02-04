package com.contact.management.exception

import org.springframework.http.HttpStatus

enum class CommonExceptionCode(
    val status: HttpStatus,
    val message: String,
) {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 오류가 발생했습니다.")
}
