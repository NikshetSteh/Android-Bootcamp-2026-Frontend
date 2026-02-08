package com.example.create_meet.domain

import com.example.create_meet.data.dto.MeetingResponse
import kotlinx.coroutines.flow.StateFlow


interface MeetingRepository {
    suspend fun getSchedule()

    val meetings: StateFlow<List<MeetingResponse>>

}


