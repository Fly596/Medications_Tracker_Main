package com.galeria.medicationstracker.ui.screens.profile.profiledetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.ui.components.GBasicTextField
import com.galeria.medicationstracker.ui.components.GPrimaryButton
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.utils.formatDateStringToTimestampMMMMddyyyy
import com.galeria.medicationstracker.utils.formatTimestampTillTheDayMMMMddyyyy
import com.google.firebase.Timestamp

@Composable
fun ProfileDetailsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: ProfileDetailsViewModel,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    onBackClick.invoke()
                    // Handle back button click
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = MedTrackerTheme.colors.sysBlack,
                    modifier = Modifier,
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Health Details",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier,
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Image(
            painter = painterResource(id = R.drawable.img_1543), // Replace with your drawable
            contentDescription = "Profile Icon",
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(bottom = 16.dp).clip(CircleShape).size(108.dp),
        )

        HealthDetailItem("First Name", state.value.firstName ?: "") {
            viewModel.updateFirstName(it)
        }
        HealthDetailItem("Last Name", state.value.lastName ?: "") { viewModel.updateLastName(it) }
        HealthDetailItem("Email", state.value.email ?: "") { viewModel.updateEmail(it) }

        HealthDetailItem(
            "Date of Birth",
            formatTimestampTillTheDayMMMMddyyyy(state.value.dateOfBirth ?: Timestamp.now()),
        ) {
            viewModel.updateDateOfBirth(formatDateStringToTimestampMMMMddyyyy(it))
        }
        HealthDetailItem("Sex", state.value.sex?.toString() ?: "") { viewModel.updateSex(it) }

        HealthDetailItem("Weight", state.value.weight?.toString() ?: "") {
            viewModel.updateWeight(it.toFloat())
        }
        HealthDetailItem("Height", state.value.height?.toString() ?: "") {
            viewModel.updateHeight(it.toFloat())
        }

        GPrimaryButton(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            onClick = { viewModel.updateUser() },
        ) {
            Text("Update")
        }

        Text(
            text =
                "Track pushes instead of steps on Apple Watch in the Activity app, and in wheelchair workouts in the Workout app, and record them to Health. When this setting is on, your...",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
fun HealthDetailItem(label: String, value: Any, onValueChange: (String) -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        GBasicTextField(
            value = value.toString(),
            onValueChange = { onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            interactionSource = interactionSource,
            prefix = label,
            prefixModifier = Modifier.padding(end = 16.dp),
        )
    }
}

@Preview
@Composable
fun ProfileDetailsScreenPreview() {
    MedTrackerTheme {}
}
