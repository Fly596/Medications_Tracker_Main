package com.galeria.medicationstracker.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.data.UserIntake
import com.galeria.medicationstracker.data.UserMedication
import com.galeria.medicationstracker.ui.components.GPrimaryButton
import com.galeria.medicationstracker.ui.components.GSecondaryButton
import com.galeria.medicationstracker.ui.componentsOld.FlySimpleCard
import com.galeria.medicationstracker.ui.screens.dashboard.record.LogsCard
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.colors
import com.galeria.medicationstracker.utils.formatTimestampTillTheDayMMMMddyyyy
import com.galeria.medicationstracker.utils.formatTimestampTillTheHour

@Composable
fun AccountScreenHead(
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {},
    onNotesClick: () -> Unit = {},
    viewModel: ProfileVM,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        // pfp, name, email.
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // pfp, name, email.
            Row(
                modifier = Modifier.padding(bottom = 24.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.img_1543),
                    contentDescription = "pfp",
                    contentScale = ContentScale.Crop,
                    modifier = modifier.clip(CircleShape).size(108.dp),
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = uiState.value.user?.firstName.toString(),
                        style = MedTrackerTheme.typography.display3Emphasized,
                        color = colors.primaryLabel,
                    )
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        // LabeledStat(uiState.value.user?.dateOfBirth.toString(), "Age")
                        LabeledStat(uiState.value.user?.height.toString(), "Heigth")
                        LabeledStat(uiState.value.user?.weight.toString(), "Weight")
                    }
                }
            }

            GPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onProfileClick.invoke()
                    // Todo: open edit profile screen
                },
            ) {
                Text(text = "Edit Profile")
            }
            GSecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onNotesClick.invoke() },
            ) {
                Text(text = "Notes")
            }
        }
        // menu items.
        TabsRow(
            modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
            tabs = listOf("Medications", "History"),
            medications = uiState.value.medications,
            intakes = uiState.value.intakes,
        )
    }
}

@Composable
fun TabsRow(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    medications: List<UserMedication> = emptyList(),
    intakes: List<UserIntake> = emptyList(),
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = colors.primaryBackground,
            contentColor = colors.sysBlack,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = colors.sysBlack,
                )
            },
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) },
                    selectedContentColor = colors.primaryLabel,
                    unselectedContentColor = colors.secondaryLabel,
                )
            }
        }

        when (selectedTabIndex) {
            0 -> UserMedications(medications)
            1 -> UserHistory(intakes)
        }
    }
}

@Composable
fun UserMedications(mediations: List<UserMedication> = emptyList()) {
    LazyColumn() { items(mediations) { medication -> MedicationCard(medication = medication) } }
}

@Composable
fun UserHistory(intakes: List<UserIntake> = emptyList()) {
    LazyColumn {
        items(intakes) { intake ->
            val formattedDate =
                if (intake.dateTime != null) {
                    formatTimestampTillTheDayMMMMddyyyy(intake.dateTime)
                } else {
                    ""
                }
            val formatedTime =
                if (intake.dateTime != null) {
                    formatTimestampTillTheHour(intake.dateTime)
                } else {
                    ""
                }
            LogsCard(
                name = intake.medicationName.toString(),
                status = intake.status.toString(),
                date = formattedDate,
                time = formatedTime,
            )
        }
    }
}

@Composable
fun MedicationCard(modifier: Modifier = Modifier, medication: UserMedication? = null) {
    FlySimpleCard(modifier = modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocalPharmacy,
                contentDescription = "Pill Icon",
                modifier = Modifier.size(56.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = medication?.name.toString(),
                        style = MedTrackerTheme.typography.bodyLargeEmphasized,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = medication?.strength.toString() + "mg",
                        style = MedTrackerTheme.typography.bodyLarge,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = medication?.intakeTime.toString(),
                    style = MedTrackerTheme.typography.bodyLarge,
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.width(250.dp),
                    text = medication?.daysOfWeek.toString().lowercase(),
                    style = MedTrackerTheme.typography.bodyMedium,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(modifier = Modifier, onClick = {}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "More Info",
                    tint = MedTrackerTheme.colors.primaryLabel,
                    modifier = Modifier.size(36.dp),
                )
            }
        }
    }
}

@Composable
fun LabeledStat(count: String, label: String) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = count,
            style = MedTrackerTheme.typography.bodyLargeEmphasized,
            color = MedTrackerTheme.colors.primaryLabel,
        )
        Text(
            text = label,
            style = MedTrackerTheme.typography.bodyLarge,
            color = MedTrackerTheme.colors.primaryLabel,
        )
    }
}

@Composable
@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_8")
fun AccScreenPreview() {
    MedTrackerTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            // AccountScreenHead()
        }
    }
}
