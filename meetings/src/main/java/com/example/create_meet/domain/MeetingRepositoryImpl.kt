package com.example.create_meet.domain

import com.example.create_meet.data.dto.MeetingResponse
import com.example.create_meet.data.MeetingsNetworkDataSource
import com.example.create_meet.data.dto.MeetingsResult
import com.example.token_storage.domain.TokenRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val network: MeetingsNetworkDataSource,
    private val tokenRepository: TokenRepository
) : MeetingRepository {

    private val _meetings = MutableStateFlow<List<MeetingResponse>>(emptyList())
    override val meetings: StateFlow<List<MeetingResponse>> = _meetings

    override suspend fun getSchedule() {
        val token = tokenRepository.getToken()
        if (token != null) {
            when (val result = network.getSchedule(token)) {
                is MeetingsResult.Success -> {
                    _meetings.value = result.meetings
                }

                is MeetingsResult.Error -> {
                    throw RuntimeException(result.message)
                }
            }
        } else {
            throw RuntimeException()
        }

    }
}


