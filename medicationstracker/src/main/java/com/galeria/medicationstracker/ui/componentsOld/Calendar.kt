package com.galeria.medicationstracker.ui.componentsOld

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galeria.medicationstracker.ui.components.GOutlinedButton
import com.galeria.medicationstracker.ui.screens.medications.newmed.AddNewMedViewModel
import com.galeria.medicationstracker.ui.screens.medications.update.UpdateMedVM
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.colors
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun WeeklyCalendarView(modifier: Modifier = Modifier) {

  // Get today's date.
  val currentDate = LocalDate.now()
  val dateFormatter = DateTimeFormatter.ofPattern("dd")
  val dayOfWeekFormatter =
    DateTimeFormatter.ofPattern("EE") // Add day of week formatter

  LazyRow(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    items(7) { index ->
      val date = currentDate.plus(
        index.toLong(),
        ChronoUnit.DAYS
      ) // Calculate date for each item
      val formattedDate = date.format(dateFormatter)
      val isSelected = date == currentDate // Check if it's the current date

      CalendarDayItem(
        dayOfWeek = date.format(dayOfWeekFormatter),
        date = formattedDate,
        isSelected = isSelected
      )
    }
  }
}

@Composable
fun CalendarDayItem(dayOfWeek: String, date: String, isSelected: Boolean) {

  // Style for selected and unselected days
  val backgroundColor = if (isSelected) {
    colors.primaryBackground
  } else {
    colors.secondaryBackground
  }
  val textColor = if (isSelected) {
    colors.primary600
  } else {
    colors.primaryLabel
  }

  Box(
    modifier = Modifier
      .background(backgroundColor, shape = RoundedCornerShape(4.dp))
      .padding(12.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      modifier = Modifier,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = dayOfWeek,
        style = MedTrackerTheme.typography.bodySmall,
        color = textColor
      )
      Text(
        text = date,
        style = MedTrackerTheme.typography.bodyMedium,
        color = textColor
      )
    }
  }
}

// TODO: remove viewmodel.
@Composable
fun DayOfWeekSelector(
    viewModel: AddNewMedViewModel? = null,
    viewModelUpd: UpdateMedVM? = null,
) {
  val daysOfWeek = DayOfWeek.entries
  val selectedDays = remember { mutableStateListOf<String>() }

  Column {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      daysOfWeek.forEach { dayOfWeek ->
        DayItem(
          dayOfWeek = dayOfWeek,
          isSelected = selectedDays.contains(dayOfWeek.toString()),
          onClick = {
            if (selectedDays.contains(dayOfWeek.toString())) {
              selectedDays.remove(dayOfWeek.toString())
            } else {
              selectedDays.add(dayOfWeek.toString())
            }
          }
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))
    
    GOutlinedButton(
      onClick = {
        if (viewModel != null) {
          viewModel.updateSelectedDays(selectedDays.toList())
        } else {
          viewModelUpd?.updateSelectedDays(selectedDays.toList())
        }
      })
    {
      Text("Confirm")
    }
  }
}

@Composable
fun DayItem(
    dayOfWeek: DayOfWeek,
    isSelected: Boolean,
    onClick: () -> Unit
) {
  val backgroundColor =
    if (isSelected) colors.sysBlack else colors.primaryBackground
  val textColor =
    if (isSelected) colors.sysWhite else colors.primaryLabel

  Box(
    modifier = Modifier
      .size(40.dp)
      .clip(RoundedCornerShape(8.dp))
      .background(backgroundColor)
      .clickable { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = dayOfWeek.name.substring(0, 1), // Get the first letter of the day
      color = textColor
    )
  }
}

@Preview(
  showBackground = true, device = "spec:width=411dp,height=891dp", showSystemUi = true,
  uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO or android.content.res.Configuration.UI_MODE_TYPE_NORMAL,
  backgroundColor = 0xFFFFFFFF
)
@Composable
fun DayItemSelectedPreview() {
  MedTrackerTheme {
    Column(modifier = Modifier.fillMaxSize()) {
      WeeklyCalendarView()
      /*       DayOfWeekSelector()
      
            DayItem(
              dayOfWeek = DayOfWeek.MONDAY,
              isSelected = false,
              onClick = { }
            )
            DayItem(
              dayOfWeek = DayOfWeek.MONDAY,
              isSelected = true,
              onClick = { }
            ) */
    }

  }
}
