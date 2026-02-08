package com.example.create_meet.presentation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.create_meet.data.MeetingResponse
import com.example.create_meet.data.MeetingStatus
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsListScreen(
    viewModel: EventsListScreenViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val state = uiState // Capture for smart cast

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Расписание встреч") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.refresh() }) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
        }
    ) { padding ->
        Box(modifier = modifier.padding(padding)) {
            when (state) {
                is EventsUiState.NotLoaded -> {
                    Log.d("EVENTS", "Not loaded ststate")
                    LaunchedEffect(state) {
                        Log.d("EVENTS", "Refresh run")
                        viewModel.refresh()
                    }
                }
                is EventsUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                }

                is EventsUiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = state.message)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.refresh() }) {
                            Text("Повторить")
                        }
                    }
                }

                is EventsUiState.Success -> {
                    if (state.meetings.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Нет запланированных встреч")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(
                                count = state.meetings.size,
                            )
                            {
                                MeetingItem(meeting = state.meetings[it])

                            }
//                            items(
//                                items = state.meetings,
//                                key = { it.id }
//                            ) { meeting ->
//                                MeetingItem(meeting = meeting)
//                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MeetingItem(
    meeting: MeetingResponse,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = meeting.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatMeetingTime(meeting.startTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (!meeting.description.isNullOrBlank()) {
                Text(
                    text = meeting.description!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = "📍 ${meeting.location}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = when (meeting.status) {
                    MeetingStatus.SCHEDULED -> "Назначена"
                    MeetingStatus.CANCELLED -> "Отменена"
                    MeetingStatus.COMPLETED -> "Завершена"
                },
                color = when (meeting.status) {
                    MeetingStatus.SCHEDULED -> MaterialTheme.colorScheme.primary
                    MeetingStatus.CANCELLED -> MaterialTheme.colorScheme.error
                    MeetingStatus.COMPLETED -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )

            if (meeting.invitations.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Участники: ${meeting.invitations.size}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun formatMeetingTime(isoString: String): String {
    return try {
        val instant = Instant.parse(isoString)
        val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
        DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm").format(localDateTime)
    } catch (e: Exception) {
        Log.e("EventsListScreen", "Failed to parse time: $isoString", e)
        isoString
    }
}