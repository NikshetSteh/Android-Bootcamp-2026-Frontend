package com.example.user_main.domain

import kotlinx.coroutines.flow.StateFlow

interface UserRepository {

    val currentUser: StateFlow<User?>

    suspend fun loadUser(): UserResult

    suspend fun updateUser(user: User): UserResult

    suspend fun clear()
}

