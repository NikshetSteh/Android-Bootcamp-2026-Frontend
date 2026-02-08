package com.example.user_main.domain

import com.example.comon.UserDto
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun map(dto: UserDto): User =
        User(
            fullName = dto.fullName,
            phoneNumber = dto.phoneNumber,
            department = dto.department
        )
}

data class User(
    val fullName: String,
    val phoneNumber: String,
    val department: String
)