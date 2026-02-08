package com.example.create_meet.presentation


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.create_meet.data.InvitationResult
import com.example.create_meet.domain.use_cases.AcceptInvitationUseCase
import com.example.create_meet.domain.use_cases.DeclineInvitationUseCase
import com.example.create_meet.domain.use_cases.LoadMeetingsUseCase
import com.example.create_meet.domain.use_cases.ObserveMeetingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EventsListScreenViewModel @Inject constructor(
    observeMeetings: ObserveMeetingsUseCase,
    private val loadMeetings: LoadMeetingsUseCase,
    private val acceptInvitationUseCase: AcceptInvitationUseCase,
    private val declineInvitationUseCase: DeclineInvitationUseCase
    ) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _error = MutableStateFlow<Throwable?>(null)
    private val _actionState = MutableStateFlow<ActionState>(ActionState.Idle)
    val actionState: StateFlow<ActionState> = _actionState
    val uiState: StateFlow<EventsUiState> =
        observeMeetings()
            .combine(_error) { meetings, error ->
                when {
                    error != null ->
                        EventsUiState.Error(error.message ?: "Ошибка загрузки")

                    else ->
                        EventsUiState.Success(meetings)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = EventsUiState.Loading
            )

    init {
        refresh()
    }
    fun resetActionState() {
        _actionState.value = ActionState.Idle
    }
    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _error.value = null
            Log.d("Refresh","$_isRefreshing")

            try {
                loadMeetings()
            } catch (e: Exception) {
                _error.value = e
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun accept(invitationId: String) {
        viewModelScope.launch {
            _actionState.value = ActionState.Loading
            when(val result = acceptInvitationUseCase(invitationId)) {
                InvitationResult.Success -> _actionState.value = ActionState.Success
                is InvitationResult.Error -> _actionState.value = ActionState.Error(result.message)
            }
        }
    }

    fun decline(invitationId: String) {
        viewModelScope.launch {
            _actionState.value = ActionState.Loading
            when(val result = declineInvitationUseCase(invitationId)) {
                InvitationResult.Success -> _actionState.value = ActionState.Success
                is InvitationResult.Error -> _actionState.value = ActionState.Error(result.message)
            }
        }
    }
}

