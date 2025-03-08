package com.galeria.medicationstracker.ui.screens.setting.appsettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galeria.medicationstracker.ui.componentsOld.HIGListButton
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  modifier: Modifier = Modifier,
  // viewModel: SettingsViewModel = viewModel()
) {
  Column(
    modifier = modifier
      .fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Spacer(modifier = Modifier.padding(8.dp))
    
    HIGListButton(
      text = "Adjust Design",
      onClick = {
        // TODO: Navigates to screen.
      },
      leftIcon = Icons.Filled.Visibility
    )
    
    HIGListButton(
      text = "Select Start Page",
      onClick = {
        // TODO: Navigates to screen.
      },
      leftIcon = Icons.Filled.Start
    )
    
    HIGListButton(
      text = "Change Language",
      onClick = {
        // TODO: Navigates to screen.
      },
      leftIcon = Icons.Filled.Language
    )
  }
  
}

@Preview
@Composable
fun SettingsScreenPreview() {
  /*   SettingsScreen(
      modifier = Modifier
        .fillMaxSize()
    ) */
}

@Composable
fun SettingsCardComponent(
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
  title: String = "category name",
  onClick: () -> Unit,
) {
  ElevatedCard(modifier = modifier.padding(vertical = 8.dp)) {
    Column(
      modifier =
        Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
      // verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          if (icon != null) {
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = MedTrackerTheme.colors.secondary600,
              modifier = Modifier.size(24.dp),
            )
          }
          
          Text(title, style = MedTrackerTheme.typography.bodyMedium)
        }
        
        Spacer(modifier.weight(1f))
        
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
          contentDescription = null,
          tint = MedTrackerTheme.colors.separator,
          modifier = Modifier.size(24.dp),
        )
      }
      // Text(description, style = MedTrackerTheme.typography.subhead)
    }
  }
  
}