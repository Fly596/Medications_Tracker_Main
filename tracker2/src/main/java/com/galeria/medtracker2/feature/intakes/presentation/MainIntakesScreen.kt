package com.galeria.medtracker2.feature.intakes.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.core.common.DateTimeUtils
import com.galeria.medtracker2.core.common.data.FullSchedule
import com.galeria.medtracker2.core.ui.theme.SpeechRecognitionAppTheme
import java.util.UUID

@Composable
fun MainIntakesScreen(
    onAddMedicationClick: () -> Unit = {},
    viewModel: MainIntakesVM = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = onAddMedicationClick,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
        ) {
            Text("On add med page")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Ближайшие приемы",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        if (state.plannedIntakes.isEmpty() && !state.isLoading) {
            EmptySchedulePlaceholder()
        } else {
            IntakeList(state.plannedIntakes)
        }

        // Полный список приемов.

    }
}

@Composable
fun IntakeList(intakes: List<FullSchedule>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = intakes, key = { it.idDateTime }) { intake -> IntakeCard(intake) }
    }
}

@Composable
fun IntakeCard(intake: FullSchedule, onCheck: (Boolean) -> Unit = {}) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        CheckIntakeDialog(intake = intake, onCheck = {})
    } else {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = intake.name, style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "${intake.doseMg} mg",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                // Форматирование даты и времени
                val formattedTime =
                    DateTimeUtils.fromTimestampToLocalDateTime(intake.scheduledIntakeDateTime)
                        .format(DateTimeUtils.dateTimeFormatter)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    // TODO: функционал отметки приема.
                    Button(
                        onClick = {
                            // open intake dialog.
                            showDialog = !showDialog
                        },
                        shape = RoundedCornerShape(percent = 100),
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "check intake")
                    }
                }
            }
        }
    }
}

@Composable
fun CheckIntakeDialog(intake: FullSchedule, onCheck: (Boolean) -> Unit) {

    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = intake.name, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "${intake.doseMg} mg",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom,
            ) {
                Button(onClick = { onCheck(true) }) { Text("Confirm") }
                Button(onClick = { onCheck(false) }) { Text("Skip") }
            }
        }
    }
}

@Composable
private fun EmptySchedulePlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("На сегодня приемов нет", style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpeechRecognitionAppTheme {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(4) {
                IntakeCard(
                    intake =
                        FullSchedule(
                            idDateTime = UUID.randomUUID(),
                            idRegiment = UUID.randomUUID(),
                            name = "Name",
                            doseMg = 56.0,
                            scheduledIntakeDateTime = 0,
                        )
                )
            }
        }
    }
}
