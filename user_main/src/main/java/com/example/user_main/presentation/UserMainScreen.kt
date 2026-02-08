package com.example.user_main.presentation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen(
    viewModel: UserMainScreenViewModel,
    onLogoutClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDialogVisible by viewModel.isEditDialogVisible.collectAsState()

    val editFullName by viewModel.editFullName.collectAsState()
    val editDepartment by viewModel.editDepartment.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        when (uiState) {
            is UserUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is UserUiState.Error -> {
                Text(
                    text = (uiState as UserUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is UserUiState.Success -> {
                val user = (uiState as UserUiState.Success).user
                ProfileContent(
                    user = user,
                    onEditClick = { viewModel.openEditDialog() },
                    onLogoutClick = onLogoutClick
                )
            }

            is UserUiState.NotLoaded -> {
                Log.d("USER", "Not loaded state")
                LaunchedEffect(uiState) {
                    Log.d("USER", "Run load user")
                    viewModel.loadUser()
                }
            }
        }

        if (isDialogVisible) {
            EditUserDialog(
                fullName = editFullName,
                department = editDepartment,
                onFullNameChange = viewModel::onFullNameChange,
                onDepartmentChange = viewModel::onDepartmentChange,
                onDismiss = viewModel::closeEditDialog,
                onSave = viewModel::saveChanges
            )
        }
    }
}

