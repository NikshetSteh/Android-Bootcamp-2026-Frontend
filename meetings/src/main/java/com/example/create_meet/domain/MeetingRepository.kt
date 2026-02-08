package com.example.create_meet.domain

import com.example.create_meet.data.MeetingResponse
import com.example.create_meet.data.MeetingsResult
import kotlinx.coroutines.flow.StateFlow


interface MeetingRepository {
    suspend fun getSchedule()

    val meetings: StateFlow<List<MeetingResponse>>

}


