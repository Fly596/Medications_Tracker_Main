package com.galeria.medicationstracker.ui.componentsOld

import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.utils.navigation.HomeScreen
import com.galeria.medicationstracker.utils.navigation.MedicationScreen
import com.galeria.medicationstracker.utils.navigation.ProfileScreen

// Компоненты навигации между экранами.
enum class BottomNavigation(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val route: Any,
) {

    DASHBOARD(
        "Dashboard",
        selectedIcon = R.drawable.home_fill,
        unselectedIcon = R.drawable.home,
        route = HomeScreen.TodayMedications,
    ),
    MEDICATIONS(
        "Medications",
        selectedIcon = R.drawable.lab_profile_fill,
        unselectedIcon = R.drawable.lab_profile,
        route = MedicationScreen.MedicationsList,
    ),
    PROFILE(
        "Profile",
        selectedIcon = R.drawable.profile_fill,
        unselectedIcon = R.drawable.profile,
        route = ProfileScreen.ProfileMain,
    ),
}
