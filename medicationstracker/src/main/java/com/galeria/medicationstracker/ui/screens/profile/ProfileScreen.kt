package com.galeria.medicationstracker.ui.screens.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.*
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.data.*
import com.galeria.medicationstracker.ui.componentsOld.*
import com.galeria.medicationstracker.ui.theme.*

/**
 * Represents the user's profile screen.
 *
 * This screen displays the user's profile information, including their
 * profile picture, name, and email. It also provides options for
 * managing notifications and app settings, as well as a logout button.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param onDoctorClick A callback function that is invoked when the user clicks on the settings option.
 * @param onNotificationsClick A callback function that is invoked when the user clicks on the notifications option.
 * @param viewModel The ViewModel for this screen. Defaults to a new instance of ProfileVM.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onDoctorClick: () -> Unit = {},
    onWeightClick: () -> Unit = {},
    onHeightClick: () -> Unit = {},
    viewModel: ProfileVM,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val doctorsList = uiState.value.doctors

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Display header with profile picture and name.
        /*         Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start,
                ) {


                    // User's profile information.
                    PfpWithName(
                        // TODO: get from firebase.
                        painter = R.drawable.img_1543,
                        userName = uiState.value.user?.name.toString(),
                        userEmail = uiState.value.user?.login.toString()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                } */
        // HealthCardsGrid()
        // Section title.
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // title and "edit" button.
            Text(
                text = "Profile",
                style = MedTrackerTheme.typography.largeTitleEmphasized
            )
            FlyTextButton(
                onClick = {
                    /* TODO: open health */
                },
            ) {
                Text(text = "Edit")
            }
        }
        // pfp, name, email.
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.img_1543),
                contentDescription = "pfp",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .clip(CircleShape)
                    .size(108.dp),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = uiState.value.user?.name.toString(),
                    style = MedTrackerTheme.typography.title1Emphasized,
                    color = MedTrackerTheme.colors.primaryLabel
                )
                Text(
                    text = uiState.value.user?.login.toString(),
                    style = MedTrackerTheme.typography.body,
                    color = MedTrackerTheme.colors.tertiaryLabel
                )
            }
        }

        LazyRow(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                FlyIconButtonWithText(
                    text = "20 years old"
                )
            }
            item {
                FlyIconButtonWithText(
                    text = "150 lbs",
                    icon = R.drawable.body
                )
            }
            item {
                FlyIconButtonWithText(
                    text = "6.5 ft",
                    icon = R.drawable.body
                )
            }
        }
        /*        LazyColumn {
                   item {
                       MyTextField(
                           value = uiState.value.name,
                           onValueChange = { viewModel.updateName(it) },
                           label = "Name"
                       )
                       FlyButton(
                           onClick = { viewModel.updateNameFirestore() }
                       ) {
                           Text(text = "Save name")
                       }
                   }
                   item {
                       MyTextField(
                           value = uiState.value.age.toString(),
                           keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                           onValueChange = { viewModel.updateAge(it.toInt()) },
                           label = "Age",
                       )
                       FlyButton(
                           onClick = { viewModel.updateAgeFirestore() }
                       ) {
                           Text(text = "Save age")
                       }
                   }
                   item {
                       MyTextField(
                           value = uiState.value.weight.toString(),
                           onValueChange = { viewModel.updateWeight(it.toFloat()) },
                           label = "weight",
                           keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                       )
                       FlyButton(
                           onClick = { viewModel.updateWeightFirestore() }
                       ) {
                           Text(text = "Save weight")
                       }
                   }
                   item {
                       MyTextField(
                           value = uiState.value.height.toString(),
                           onValueChange = { viewModel.updateHeight(it.toFloat()) },
                           label = "Height",
                           keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                       )
                       FlyButton(
                           onClick = { viewModel.updateHeightFirestore() }
                       ) {
                           Text(text = "Save Height")
                       }
                   }
               } */
        // секция со списком врачей пользователя (или просто всех врачей).
        MyDoctorsList(
            listData = doctorsList,
            onDoctorClick = onDoctorClick,
            onClick = { viewModel.updateSelectedDoctor(it) }
        )
    }
}

@Composable
fun MyDoctorsList(
    modifier: Modifier = Modifier,
    listData: List<User> = listOf(),
    onDoctorClick: () -> Unit = {},
    onClick: (User?) -> Unit = {}
) {
    Column(modifier = modifier.padding(top = 8.dp)) {
        Text(
            text = "My Doctors",
            style = MedTrackerTheme.typography.title2
        )

        LazyColumn(modifier = modifier.fillMaxWidth()) {
            items(listData) { user ->
                // UserListCard(user.name.toString())
                DocListCard(
                    name = user.name.toString(),
                    speciality = user.type.toString(),
                    modifier = Modifier.clickable {
                        onClick(user)
                        onDoctorClick.invoke()
                    },
                )
            }
        }
    }

}

@Composable
fun DocListCard(
    modifier: Modifier = Modifier,
    name: String = "James",
    speciality: String = "Boobs Watcher",
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.doc_pfp_girl),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(56.dp),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
        ) {
            Text(
                text = name,
                style = MedTrackerTheme.typography.title3
            )
            Text(
                text = speciality,
                style = MedTrackerTheme.typography.footnote
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = {/* show doctor */ }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "more",
                tint = MedTrackerTheme.colors.secondaryLabel
            )
        }
    }
}

@Composable
fun HealthCardsGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            HealthCard(
                headText = "Weight",
                valueText = "145",
                unitsText = "lbs",
            )
        }
        item {
            HealthCard(
                headText = "Height",
                valueText = "6.5",
                unitsText = "ft",
                textColor = MedTrackerTheme.colors.primary400
            )
        }
    }
}

@Composable
fun HealthCard(
    headText: String = "Blood Pressure",
    valueText: String = "150/100",
    unitsText: String = "mmHg",
    textColor: Color = MedTrackerTheme.colors.sysError,
) {
    FlySimpleCard(modifier = Modifier) {
        Column(
            modifier = Modifier.padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = headText,
                    style = MedTrackerTheme.typography.bodyEmphasized,
                    color = textColor
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = Filled.ArrowForwardIos,
                        contentDescription = "more",
                        tint = MedTrackerTheme.colors.secondaryLabel
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = valueText,
                    style = MedTrackerTheme.typography.title1Emphasized
                )
                Text(
                    text = unitsText,
                    style = MedTrackerTheme.typography.footnote
                )
            }
        }
    }
}

@Composable
fun PfpWithName(
    painter: Int,
    userName: String,
    userEmail: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(painter),
            contentDescription = "pfp",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(CircleShape)
                .size(96.dp),
        )
        // Name and email.
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            // modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = userName,
                style = MedTrackerTheme.typography.title2Emphasized,
                color = MedTrackerTheme.colors.primaryLabel
            )
            Text(
                text = userEmail,
                style = MedTrackerTheme.typography.headline,
                color = MedTrackerTheme.colors.tertiaryLabel
            )
        }
    }
}

@Composable
fun ProfileOptionItem(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                // .background(color =
                // MedicationsTrackerAppTheme.systemColors.backgroundLightSecondary)
                .clickable(onClick = onClick),
    ) {
    }
}
