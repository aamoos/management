package com.contact.management.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    private val member: CustomUserDto // Change from CustomUserInfoDto to CustomUserDto
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        // RoleType에 따라 권한 리스트를 생성
        val roles = listOf(member.role).map { role -> SimpleGrantedAuthority(role.name) }
        return roles
    }

    override fun getPassword(): String = member.password // Ensure the DTO has password if needed

    override fun getUsername(): String = member.id.toString() // or member.name if that's preferred

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}