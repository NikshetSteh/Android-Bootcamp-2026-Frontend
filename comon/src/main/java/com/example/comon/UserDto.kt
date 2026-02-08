package com.example.comon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("fullName")
    val fullName: String,

    @SerialName("phoneNumber")
    val phoneNumber: String,

    @SerialName("department")
    val department: String,

    @SerialName("password")
    val password: String? = ""
)