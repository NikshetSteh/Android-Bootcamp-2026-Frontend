package com.example.create_meet.domain.use_cases

import com.example.create_meet.domain.MeetingRepository
import javax.inject.Inject

class AcceptInvitationUseCase @Inject constructor(
    private val repository: MeetingRepository
) {
    suspend operator fun invoke(invitationId: String) = repository.acceptInvitation(invitationId)
}