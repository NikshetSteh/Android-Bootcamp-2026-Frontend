package com.example.create_meet.presentation



import com.example.token_storage.domain.TokenRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.create_meet.data.MeetingsNetworkDataSource
import com.example.create_meet.data.MeetingsResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class EventsListScreenViewModel @Inject constructor(
    private val meetingsDataSource: MeetingsNetworkDataSource,
    private val tokenProvider: TokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<EventsUiState>(EventsUiState.Loading)
    val uiState: StateFlow<EventsUiState> = _uiState

    init {
        loadSchedule()
    }

    private fun loadSchedule() {
        viewModelScope.launch {
            _uiState.value = EventsUiState.Loading
            if (tokenProvider.getToken() != null) {
                when (val result = meetingsDataSource.getSchedule(tokenProvider.getToken()!!)) {
                    is MeetingsResult.Success -> {
                        _uiState.value = EventsUiState.Success(result.meetings)
                    }

                    is MeetingsResult.Error -> {
                        _uiState.value = EventsUiState.Error(result.message)
                    }
                }
            }
            else {
                _uiState.value = EventsUiState.NotLoaded
            }
        }
    }

    fun refresh() {
        loadSchedule()
    }
}