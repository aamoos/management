package com.contact.management.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class UserDto(
    val id: Long?= null,

    @field:Size(min = 2, max = 50, message = "이름은 2~50자 사이여야 합니다.")
    val name: String,

    @field:Email(message = "올바른 이메일 형식을 입력하세요.")
    val email: String
)