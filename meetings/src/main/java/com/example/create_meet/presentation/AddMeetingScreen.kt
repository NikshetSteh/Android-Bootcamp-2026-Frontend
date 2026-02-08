package com.example.create_meet.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.comon.UserDto

@Composable
fun AddMeetingScreen(
    addMeetingViewModel: AddMeetingViewModel,
    onMeetingCreated: () -> Unit
) {
    val vm = addMeetingViewModel

    if (vm.submitSuccess) {
        LaunchedEffect(Unit) {
            onMeetingCreated()

        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Создать встречу", style = MaterialTheme.typography.titleLarge)
        }

        item {
            OutlinedTextField(
                value = vm.title,
                onValueChange = { vm.title = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = vm.description,
                onValueChange = { vm.description = it },
                label = { Text("Описание") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = vm.location,
                onValueChange = { vm.location = it },
                label = { Text("Место") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = vm.dateInput,
                onValueChange = { vm.dateInput = it },
                label = { Text("Дата (yyyy‑MM‑dd)") },
                placeholder = { Text("2026‑02‑14") },
                isError = vm.dateError != null,
                modifier = Modifier.fillMaxWidth()
            )
            vm.dateError?.let {
                Text(it, color = Color.Red)
            }
        }

        item {
            OutlinedTextField(
                value = vm.timeInput,
                onValueChange = { vm.timeInput = it },
                label = { Text("Время (HH:mm)") },
                placeholder = { Text("14:30") },
                isError = vm.timeError != null,
                modifier = Modifier.fillMaxWidth()
            )
            vm.timeError?.let {
                Text(it, color = Color.Red)
            }
        }

        item {
            OutlinedTextField(
                value = vm.durationHours.toString(),
                onValueChange = { vm.durationHours = it.toIntOrNull() ?: 1 },
                label = { Text("Длительность (часы)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text("Пригласить участников", style = MaterialTheme.typography.titleMedium)
        }

        item {
            if (vm.usersLoading) {
                CircularProgressIndicator()
            } else if (vm.usersError != null) {
                Text("Ошибка: ${vm.usersError}", color = Color.Red)
            } else {
                UsersMultiSelect(
                    users = vm.users,
                    selected = vm.selectedUsers,
                    onSelectionChanged = { vm.selectedUsers = it }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            vm.submitError?.let {
                Text("Ошибка: $it", color = Color.Red)
            }
        }

        item {
            Button(
                onClick = { vm.onSubmit() },
                enabled = !vm.isSubmitting && vm.title.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (vm.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                }
                Text("Создать")
            }
        }
    }
}


@Composable
fun UsersMultiSelect(
    users: List<UserDto>,
    selected: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    Column {
        users.forEach { user ->
            val checked = user.id in selected
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        val newList = selected.toMutableList()
                        if (it) newList.add(user.id)
                        else newList.remove(user.id)
                        onSelectionChanged(newList)
                    }
                )
                Text(user.fullName, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}



