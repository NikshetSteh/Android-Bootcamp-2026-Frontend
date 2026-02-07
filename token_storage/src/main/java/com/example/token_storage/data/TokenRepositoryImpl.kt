package com.example.token_storage.data

import com.example.token_storage.domain.SecureStorage
import com.example.token_storage.domain.TokenRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepositoryImpl @Inject constructor(
    private val secureStorage: SecureStorage
): TokenRepository {
    override suspend fun getToken(): String? {
       return secureStorage.getString("access_token")
    }

    override suspend fun saveAccessToken(token: String) {
        secureStorage.putString("access_token",token)
    }

    override suspend fun getRefreshToken(): String? {
        return secureStorage.getString("refresh_token")
    }

    override suspend fun clear() {
        secureStorage.clear()
    }
}
