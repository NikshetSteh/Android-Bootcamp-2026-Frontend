package com.example.create_meet.domain

import com.example.create_meet.data.MeetingResponse
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveMeetingsUseCase @Inject constructor(
    private val repository: MeetingRepository
) {
    operator fun invoke(): StateFlow<List<MeetingResponse>> {
        return repository.meetings
    }
}


