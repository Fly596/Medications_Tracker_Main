package com.galeria.medtracker2.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.galeria.medtracker2.navigation.AppRoutes

enum class BottomNavItem(
    val route: AppRoutes,
    val label: String,
    val icon: ImageVector,
) {

    HOME(AppRoutes.Home, "Home", Icons.Filled.Home),
    MEDICATIONS(AppRoutes.MedicationsList, "Medications", Icons.AutoMirrored.Filled.List),
    ADD_MEDICATION(AppRoutes.AddMedication, "Add Medication", Icons.Filled.Add),
}
