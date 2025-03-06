package com.galeria.medicationstracker.ui.doctor.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.ui.componentsOld.FlySimpleCard
import com.galeria.medicationstracker.ui.componentsOld.FlyTextButton
import com.galeria.medicationstracker.ui.doctor.patients.PatientsListViewModel
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme

@Composable
fun DocDashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: PatientsListViewModel = viewModel(),
    onPatientsClick: () -> Unit = {}
) {
/*     val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val userid = viewModel.currentUser?.uid
    val userName = uiState.value.currentUser?.name

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        HeaderSection(
            modifier = Modifier.fillMaxWidth(),
            name = userName.toString()
        )

        AppointmentsView(
            // onSeeAllClick = onPatientsClick,
            upcomingAppointments = uiState.value.appointments
        )

        AppointmentsStats()
    } */

}

/* @Composable
fun AppointmentsView(
    modifier: Modifier = Modifier,
    onSeeAllClick: () -> Unit = {},
    upcomingAppointments: List<Appointment> = emptyList()
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Upcoming Appointments",
                style = MedTrackerTheme.typography.headline
            )
            FlyTextButton(
                onClick = { onSeeAllClick.invoke() }
            ) {
                Text(
                    text = "See All",
                    style = MedTrackerTheme.typography.bodySmallEmphasized
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(upcomingAppointments) { appointment ->
                AppointmentCard(
                    name = appointment.patient.toString(),
                    time = appointment.time.toString()
                )
            }
        }
    }
} */

@Composable
fun AppointmentCard(
    name: String,
    time: String,
    patientProfilePicture: Painter = painterResource(R.drawable.default_pfp)
) {
    FlySimpleCard(
        modifier = Modifier
            .width(132.dp)
            .height(148.dp),
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Image(
                painter = patientProfilePicture,
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )
            Text(
                text = name,
                style = MedTrackerTheme.typography.bodySmallEmphasized
            )
            Text(
                text = time,
                style = MedTrackerTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun HeaderSection(
    modifier: Modifier = Modifier,
    title: String = "Welcome Back!",
    name: String = "Gerald",
    profileImage: Painter = painterResource(id = R.drawable.g_eazy_pfp)
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = profileImage,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(
                    width = 64.dp,
                    height = 64.dp
                )
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MedTrackerTheme.typography.title3
            )
            Text(
                name,
                style = MedTrackerTheme.typography.headline
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Row() {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = null
                )
            }
            IconButton(
                onClick = {}
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.Message,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFf2f2f7,
    fontScale = 1.0f
)
@Composable
fun DocDashboardScreenPreview() {
    MedTrackerTheme {
        DocDashboardScreen()
    }
}

@Composable
fun AppointmentsStats() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Appointment Statistics",
                style = MedTrackerTheme.typography.headline
            )
            FlyTextButton(
                onClick = { /* TODO: See all appointments */ }
            ) {
                Text(
                    text = "Last 12 Month",
                    style = MedTrackerTheme.typography.labelMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                StatisticCard(
                    label = "Total",
                    value = "760"
                )
            }
            item {
                StatisticCard(
                    label = "Online",
                    value = "494",
                )
            }
            item {
                StatisticCard(
                    label = "In Person",
                    value = "266",
                )
            }
        }
    }
}

@Composable
fun StatisticCard(
    label: String,
    value: String,
) {
    FlySimpleCard(
        modifier = Modifier
            .width(132.dp)
            .height(96.dp),
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label,
                style = MedTrackerTheme.typography.bodyMedium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = value,
                    style = MedTrackerTheme.typography.title3Emphasized
                )
            }
        }
    }
}