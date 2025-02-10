package com.contact.management.entity

import com.contact.management.audit.BaseEntity
import com.contact.management.dto.SignUpRequest
import com.contact.management.dto.UserUpdateRequest
import com.contact.management.security.RoleType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
@Table(name = "users")
class User (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?= null,
    var password: String,
    var name: String,
    var age: Int,
    var email: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    var role: RoleType // Adding the role field

) : BaseEntity(){
    companion object {
        fun from(request: SignUpRequest, encoder: PasswordEncoder) = User(
            password = encoder.encode(request.password),
            name = request.name,
            age = request.age,
            email = request.email,
            role = RoleType.USER
        )
    }

    fun update(request: UserUpdateRequest, encoder: PasswordEncoder){
        this.password = encoder.encode(request.password)
        this.name = request.name
        this.age = request.age
    }

}