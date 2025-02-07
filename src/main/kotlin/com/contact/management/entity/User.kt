package com.contact.management.entity

import com.contact.management.audit.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?= null,
    var name: String,
    var email: String
) : BaseEntity()