package com.galeria.medicationstracker.ui.screens.medications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.galeria.medicationstracker.data.UserMedication
import com.galeria.medicationstracker.ui.components.GPrimaryButton
import com.galeria.medicationstracker.ui.components.GTextButton
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.colors
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.shapes
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.typography

@Composable
fun MedicationsScreen(
  modifier: Modifier = Modifier,
  onAddMedClick: () -> Unit = {},
  onViewMed: () -> Unit,
  onEditMedClick: (String) -> Unit = {},
  medicationsViewModel: MedicationsViewModel = hiltViewModel(),
  medsPagesVM: MedsPagesViewModel = viewModel(),
) {
  val uiState by medicationsViewModel.uiState.collectAsStateWithLifecycle()
  Scaffold(
    containerColor = MedTrackerTheme.colors.secondaryBackground,
    topBar = {
      // today's date.
      Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(text = "Medications", style = typography.display3Emphasized)
      }
    },
  ) { innerPadding ->
    Column(
      modifier = modifier
        .padding(innerPadding)
        .padding(horizontal = 16.dp)
    ) {
      LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(uiState.userMedications) { med ->
          FlyElevatedCardMedsList(
            title = med.name,
            dosage =
              ("${med.strength} ${
                med.unit.toString()
                  .lowercase()
              }"),
            info = med.form.toString().lowercase(),
            onEditClick = { onEditMedClick(med.name) },
            onRemoveMedClick = {
              medicationsViewModel.deleteMedicationFromFirestore(
                med.name
              )
            },
            onViewMed = {
              medsPagesVM.getSelectedMed(med.name)
              onViewMed()
            },
          )
        }
        
        item {
          // Button to add a new medication.
          GPrimaryButton(
            onClick = { onAddMedClick.invoke() },
            Modifier.fillMaxWidth()
          ) {
            Text("+ Add Medication")
          }
        }
      }
    }
  }
  /*   Column(
      modifier = modifier.fillMaxSize().padding(top = 8.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(uiState.userMedications) { med ->
          FlyElevatedCardMedsList(
            title = med.name,
            dosage =
              ("${med.strength} ${
              med.unit.toString()
                .lowercase()
            }"),
            info = med.form.toString().lowercase(),
            onEditClick = { onEditMedClick(med.name) },
            onRemoveMedClick = { medicationsViewModel.deleteMedicationFromFirestore(med.name) },
            onViewMed = {
              medsPagesVM.getSelectedMed(med.name)
              onViewMed()
            },
          )
        }
  
        item {
          // Button to add a new medication.
          GPrimaryButton(onClick = { onAddMedClick.invoke() }, Modifier.fillMaxWidth()) {
            Text("+ Add Medication")
          }
        }
      }
    } */
}

@Composable
fun FlyElevatedCardMedsList(
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
  title: String = "Medicine Name",
  dosage: String = "50 mg",
  info: String = "Mon, Tue, Fri...",
  onEditClick: () -> Unit,
  onRemoveMedClick: () -> Unit,
  onViewMed: () -> Unit,
  medication: UserMedication? = null,
  shape: Shape = shapes.medium,
  elevation: CardElevation = CardDefaults.elevatedCardElevation(),
) {
  ElevatedCard(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onViewMed.invoke() },
    shape = shape,
    elevation =
      CardDefaults.elevatedCardElevation(
        defaultElevation = 0.dp,
        pressedElevation = 8.dp,
        focusedElevation = 10.dp,
      ),
    colors =
      CardDefaults.elevatedCardColors(
        containerColor = colors.primaryBackground,
        contentColor = colors.primaryLabel,
        disabledContainerColor = colors.primaryTinted,
        disabledContentColor = colors.secondary600,
      ),
  ) {
    Row(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalAlignment = Alignment.Top
    ) {
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
      ) {
        Text(title, style = typography.headline)

        Spacer(modifier = Modifier.weight(1f))

        Text(dosage, style = typography.bodyMedium)
        Text(info, style = typography.bodyMedium)
      }

      Spacer(modifier = Modifier.weight(1f))
      
      Column(Modifier, horizontalAlignment = Alignment.End) {
        GTextButton(onEditClick) { Text("Edit") }

        GTextButton(
          errorButton = true,
          onClick = { onRemoveMedClick.invoke() },
          textStyle = typography.labelLargeEmphasized,
        ) {
          Text("Delete")
        }
      }
    }
  }
}

@Preview(backgroundColor = 0xFFF1F1F1, showBackground = true)
@Composable
fun FlyElevatedCardMedsListPreview() {
  MedTrackerTheme {
    Column(modifier = Modifier
      .padding(16.dp)
      .fillMaxSize()) {
      FlyElevatedCardMedsList(
        onEditClick = { /*TODO*/ },
        onRemoveMedClick = { /*TODO*/ },
        onViewMed = { /*TODO*/ },
      )
    }
  }
}
