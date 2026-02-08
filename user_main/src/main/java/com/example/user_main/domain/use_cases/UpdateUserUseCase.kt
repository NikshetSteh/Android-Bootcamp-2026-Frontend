package com.example.user_main.domain.use_cases

import com.example.user_main.domain.User
import com.example.user_main.domain.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) = userRepository.updateUser(user)
}


