package com.galeria.medicationstracker.ui.screens.profile.notes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.ui.components.GBasicTextField
import com.galeria.medicationstracker.ui.components.GPrimaryButton
import com.galeria.medicationstracker.ui.components.GTextField
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme

@Composable
fun NewNoteScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: NewNoteViewModel,
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    MedTrackerTheme {
        Scaffold(
            containerColor = MedTrackerTheme.colors.secondaryBackground,
            topBar = {
                Column(modifier = Modifier.padding(vertical = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            onBackClick,
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back"
                            )
                        }
                        Text(
                            text = "New Note",
                            style = MedTrackerTheme.typography.display3Emphasized,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                }
            }
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                GBasicTextField(
                    value = state.value.title,
                    onValueChange = {
                        viewModel.updateTitle(it)
                    },
                    modifier = Modifier.padding(bottom = 24.dp),
                    alignEnd = false,
                    textStyle = MedTrackerTheme.typography.title2,
                    prefix = "Title",
                    prefixStyle = MedTrackerTheme.typography.title1Emphasized,
                    prefixModifier = Modifier
                )
                
                GTextField(
                    value = state.value.content,
                    onValueChange = {
                        viewModel.updateContent(it)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    singleLine = false,
                    isPrimaryColor = true
                )
                // Medication Chips
                Text(
                    text = "Medications",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(state.value.medications) { medication ->
                        FilterChip(
                            text = medication.name,
                            isSelected = medication.name in state.value.selectedMedications,
                            onSelected = { viewModel.toggleMedication(medication.name) }
                        )
                    }
                }
                
                GPrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    onClick = {
                        viewModel.saveNote()
                    }
                ) {
                    Text("Save")
                }
            }
        }
    }
    
}

// Reusable FilterChip composable
@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MedTrackerTheme.colors.primary400 else MedTrackerTheme.colors.secondaryBackground,
        border = BorderStroke(
            1.dp,
            if (isSelected) MedTrackerTheme.colors.primary400 else MedTrackerTheme.colors.sysBlack.copy()
        ),
        modifier = modifier.clickable { onSelected(!isSelected) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSelected) MedTrackerTheme.colors.primaryLabelDark else MedTrackerTheme.colors.primaryLabel
            )
        }
    }
}