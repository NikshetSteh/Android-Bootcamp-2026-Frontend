package com.example.create_meet.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.create_meet.domain.LoadMeetingsUseCase
import com.example.create_meet.domain.ObserveMeetingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EventsListScreenViewModel @Inject constructor(
    private val observeMeetings: ObserveMeetingsUseCase,
    private val loadMeetings: LoadMeetingsUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<EventsUiState>(EventsUiState.Loading)
    val uiState: StateFlow<EventsUiState> = _uiState

    init {
        observe()
        refresh()
    }

    private fun observe() {
        viewModelScope.launch {
            observeMeetings().collect { meetings ->
                _uiState.value = EventsUiState.Success(meetings)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = EventsUiState.Loading
            try {
                loadMeetings()
            } catch (e: Exception) {
                _uiState.value =
                    EventsUiState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }
}
