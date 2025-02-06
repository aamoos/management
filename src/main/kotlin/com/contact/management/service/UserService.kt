package com.contact.management.service

import com.contact.management.dto.UserDto
import com.contact.management.entity.User
import com.contact.management.exception.CommonException
import com.contact.management.exception.CommonExceptionCode.EMAIL_ALREADY_EXISTS
import com.contact.management.exception.CommonExceptionCode.USER_NOT_FOUND
import com.contact.management.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) {

    fun getAllUsers() : List<UserDto> {
        return userRepository.findAll().map { UserDto(it.id, it.name, it.email) }
    }

    fun getUserById(id: Long): UserDto {
        val user = userRepository.findById(id).orElseThrow{ CommonException(USER_NOT_FOUND) }
        return UserDto(user.id, user.name, user.email)
    }

    fun createdUser(userDto: UserDto): UserDto {
        userRepository.findByEmail(userDto.email)?.let {
            throw CommonException(EMAIL_ALREADY_EXISTS)
        }

        val user = userRepository.save(User(name = userDto.name, email = userDto.email))
        return UserDto(user.id, user.name, user.email)
    }

    // 사용자 업데이트
    fun updateUser(id: Long, userDto: UserDto): UserDto{
        val updatedUser = userRepository.findById(id).orElseThrow{ CommonException(USER_NOT_FOUND) }

        // 엔티티의 속성 수정
        updatedUser.name = userDto.name
        updatedUser.email = userDto.email
        // DB에 업데이트 로직

        return UserDto(
            id = updatedUser.id,
            name = updatedUser.name,
            email = updatedUser.email
        )
    }

    fun deleteUser(id: Long){
        if(!userRepository.existsById(id)){
            throw CommonException(USER_NOT_FOUND)
        }
        userRepository.deleteById(id)
    }

}