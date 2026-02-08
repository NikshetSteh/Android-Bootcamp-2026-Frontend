package com.example.create_meet.presentation


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val loadMeetings: LoadMeetingsUseCase
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _error = MutableStateFlow<Throwable?>(null)

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
}

