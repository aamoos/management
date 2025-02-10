package com.contact.management.security

import com.contact.management.entity.User
import com.contact.management.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(id: String): UserDetails {
        val user: User = userRepository.findById(id.toLong())
            .orElseThrow { UsernameNotFoundException("해당하는 유저가 없습니다.") }

        // entity -> dto 변환
        val dto = CustomUserDto.from(user)

        return CustomUserDetails(dto)
    }
}