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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.ui.components.GBasicTextField
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.utils.parseDateForFirestore
import com.google.firebase.Timestamp

@Composable
fun ProfileDetailsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: ProfileDetailsViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    /*     var firstName by remember { mutableStateOf("Eugen") }
        var lastName by remember { mutableStateOf("Krylov") }
        var dateOfBirth by remember { mutableStateOf("Not Set") }
        var sex by remember { mutableStateOf("Not Set") }
        var bloodType by remember { mutableStateOf("Not Set") }
        var fitzpatrickSkinType by remember { mutableStateOf("Not Set") }
        var wheelchair by remember { mutableStateOf("Not Set") } */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    // Handle back button click
                },
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = MedTrackerTheme.colors.sysBlack,
                    modifier = Modifier
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Health Details",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Image(
            painter = painterResource(id = R.drawable.img_1543), // Replace with your drawable
            contentDescription = "Profile Icon",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .clip(CircleShape)
                .size(108.dp),
        )

        HealthDetailItem("First Name", state.value.firstName ?: "") {
            viewModel.updateFirstName(it)
        }
        HealthDetailItem("Last Name", state.value.lastName ?: "") {
            viewModel.updateLastName(it)
        }
        HealthDetailItem("Email", state.value.email ?: "") {
            viewModel.updateEmail(it)
        }


        HealthDetailItem("Date of Birth", state.value.dateOfBirth ?: Timestamp.now()) {
            viewModel.updateDateOfBirth(
                parseDateForFirestore(it)
            )
        }
        HealthDetailItem("Sex", state.value.sex?.toString() ?: "") {
            viewModel.updateSex(it)
        }
        /*        HealthDetailItem("Blood Type", state.value.bloodType?.toString() ?: ""){
                   viewModel.updateBloodType(it)
               } */
        HealthDetailItem("Weight", state.value.weight?.toString() ?: "") {
            viewModel.updateWeight(it.toFloat())
        }
        HealthDetailItem("Height", state.value.height?.toString() ?: "") {
            viewModel.updateHeight(it.toFloat())
        }

        Text(
            text = "Track pushes instead of steps on Apple Watch in the Activity app, and in wheelchair workouts in the Workout app, and record them to Health. When this setting is on, your...",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun HealthDetailItem(label: String, value: Any, onValueChange: (String) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        GBasicTextField(
            value = value.toString(),
            onValueChange = { onValueChange(value.toString()) },
            modifier = Modifier.fillMaxWidth(),
            interactionSource = interactionSource,
            prefix = label,
            prefixModifier = Modifier.padding(end = 16.dp)
        )
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailsScreenPreview() {
    MedTrackerTheme {
        ProfileDetailsScreen()
    }
}
/*
@Composable
fun HealthDetailItem2(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Divider(color = Color.Gray.copy(alpha = 0.3f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(text = value, fontSize = 16.sp, color = Color.Gray)
        }
    }
}
*/
