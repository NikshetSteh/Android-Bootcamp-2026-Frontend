package com.example.user_main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user_main.domain.use_cases.LoadUserUseCase
import com.example.user_main.domain.use_cases.LogoutUseCase
import com.example.user_main.domain.use_cases.ObserveUserUseCase
import com.example.user_main.domain.use_cases.UpdateUserUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMainScreenViewModel @Inject constructor(
    observeUserUseCase: ObserveUserUseCase,
    private val loadUserUseCase: LoadUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val logoutUseCase: LogoutUseCase

) : ViewModel() {

    private val userFlow = observeUserUseCase()

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()
    private val _isEditDialogVisible = MutableStateFlow(false)
    val isEditDialogVisible: StateFlow<Boolean> = _isEditDialogVisible.asStateFlow()

    var editFullName = MutableStateFlow("")
        private set

    var editDepartment = MutableStateFlow("")
        private set

    init {
        observeUser()
        loadUser()
    }
    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
    private fun observeUser() {
        viewModelScope.launch {
            userFlow.collect { user ->
                if (user != null) {
                    _uiState.value = UserUiState.Success(user)
                }
            }
        }
    }

    fun loadUser() {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            val result = loadUserUseCase()

            if (result is com.example.user_main.domain.UserResult.Error) {
                _uiState.value = UserUiState.Error(result.message)
            }
        }
    }

    fun openEditDialog() {
//        val user = (uiState.value as? UserUiState.Success)?.user ?: return
        editFullName.value = "Иванов Иван"
        editDepartment.value = "Отдел разработки"
        _isEditDialogVisible.value = true
    }

    fun closeEditDialog() {
        _isEditDialogVisible.value = false
    }

    fun onFullNameChange(value: String) {
        editFullName.value = value
    }

    fun onDepartmentChange(value: String) {
        editDepartment.value = value
    }

    fun saveChanges() {
        val currentUser = (uiState.value as? UserUiState.Success)?.user ?: return

        viewModelScope.launch {
            _uiState.value = UserUiState.Loading

            val updatedUser = currentUser.copy(
                fullName = editFullName.value,
                department = editDepartment.value
            )

            when (val result = updateUserUseCase(updatedUser)) {
                is com.example.user_main.domain.UserResult.Success -> {
                    _isEditDialogVisible.value = false
                }
                is com.example.user_main.domain.UserResult.Error -> {
                    _uiState.value = UserUiState.Error(result.message)
                }
            }
        }
    }
}
