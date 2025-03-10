package com.galeria.medicationstracker.ui.screens.profile.notes

import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.ui.components.GTextField
import com.galeria.medicationstracker.ui.componentsOld.FlySimpleCard
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.utils.formatTimestampToMinutemmmmddyyyyhm

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNewNoteClick: () -> Unit = {},
    viewModel: NotesScreenViewModel,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        // title and "edit" button.
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = "Notes", style = MedTrackerTheme.typography.display3Emphasized)
            IconButton(modifier = Modifier, onClick = { onNewNoteClick.invoke() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "new note",
                    tint = MedTrackerTheme.colors.primaryLabel,
                    modifier = Modifier.size(36.dp),
                )
            }
        }

        LazyColumn() {
            items(uiState.value.notes.size) { note ->
                UserNoteCard(
                    title = uiState.value.notes[note].title.toString(),
                    content = uiState.value.notes[note].content.toString(),
                    date = formatTimestampToMinutemmmmddyyyyhm(uiState.value.notes[note].date),
                    tags = uiState.value.notes[note].tags,
                    medication = uiState.value.notes[note].medication,
                )
            }
        }
    }
}

@Composable
fun UserNoteCard(
    modifier: Modifier = Modifier,
    title: String = "Title",
    content: String = "Content",
    date: String = "Date",
    tags: List<String> = emptyList(),
    medication: List<String> = emptyList(),
    onContentValueChange: (String) -> Unit = {},
    onTitleValueChange: (String) -> Unit = {},
) {
    var tfVal by remember { mutableStateOf("content") }
    val interactionSource = remember { MutableInteractionSource() }
    FlySimpleCard(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier) {
            Text(text = date, style = MedTrackerTheme.typography.title3)
            Text(text = title, style = MedTrackerTheme.typography.title1Emphasized)

            Spacer(modifier = Modifier.height(4.dp))
            GTextField(
                value = content,
                onValueChange = { onContentValueChange(it) },
                label = "Content",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_8")
fun NotesScreenPreview() {
    MedTrackerTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            // NotesScreen()
        }
    }
}
