package com.example.create_meet.data

import com.example.comon.Network
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


import javax.inject.Inject

class MeetingsNetworkDataSource @Inject constructor(
    private val network: Network
){

    suspend fun getSchedule(token: String): MeetingsResult = withContext(Dispatchers.IO) {
        try {
            val response = network.client.get("${network.HOST}/meetings/schedule") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            return@withContext if (response.status.isSuccess()) {
                val meetings = response.body<List<MeetingResponse>>()
                MeetingsResult.Success(meetings)
            } else {
                MeetingsResult.Error("Ошибка получения расписания: ${response.status.value}")
            }
        } catch (e: Exception) {
            MeetingsResult.Error(e.message ?: "Ошибка сети")
        }
    }
}

