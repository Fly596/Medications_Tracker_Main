package com.galeria.medicationstracker.ui.componentsOld

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.ui.HeadViewModel
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.utils.navigation.Routes

data class BottomNavItem(
    val title: String,
    val route: Routes.PatientRoutes,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val hasNews: Boolean = false,
    val badgeCount: Int? = null
)

fun bottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem(
            title = "Dashboard",
            route = Routes.PatientRoutes.PatientHome,
            selectedIcon = R.drawable.home_fill,
            unselectedIcon = R.drawable.home
        ),
        BottomNavItem(
            title = "Medications",
            route = Routes.PatientRoutes.PatientMedications,
            selectedIcon = R.drawable.lab_profile_fill,
            unselectedIcon = R.drawable.lab_profile,
            hasNews = false,
            // badgeCount = 16,
        ),
        BottomNavItem(
            title = "Profile",
            route = Routes.PatientRoutes.PatientProfile,
            selectedIcon = R.drawable.profile_fill,
            unselectedIcon = R.drawable.profile,
            hasNews = false,
        ),
        // ... (other items)
    )
}

@Composable
fun BottomNavBar(
    navItems: List<BottomNavItem>,
    navController: NavHostController,
    viewModel: HeadViewModel,
) {
    val currentNavItemIndex = viewModel.selectedItemIndex.collectAsState().value

    Column {
        NavigationBar(
            // modifier = Modifier.fillMaxWidth(),
            containerColor = MedTrackerTheme.colors.primaryBackground,
            contentColor = MedTrackerTheme.colors.primaryLabel,
        ) {
            navItems.forEachIndexed { navItemIndex, navItem ->
                NavigationBarItem(
                    selected = currentNavItemIndex == navItemIndex,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MedTrackerTheme.colors.primaryTinted
                    ),
                    onClick = {
                        viewModel.updateSelectedItemIndex(navItemIndex)
                        navController.navigate(navItem.route)
                    },
                    label = {
                        Text(
                            text = navItem.title,
                            style = MedTrackerTheme.typography.bodyMedium
                        )
                    },
                    icon = {
                        IconWithBadge(
                            icon = if (navItemIndex == currentNavItemIndex) navItem.selectedIcon else navItem.unselectedIcon,
                            badgeCount = navItem.badgeCount,
                            showUnreadBadge = navItem.hasNews,
                            contentDescription = navItem.title,
                        )
                    },
                )
            }
        }
    }
    // TODO: Change colors

}

@Composable
fun IconWithBadge(
    icon: Int,
    badgeCount: Int?,
    showUnreadBadge: Boolean,
    contentDescription: String?,
) {
    BadgedBox(
        badge = {
            when {
                badgeCount != null -> Badge { Text(text = badgeCount.toString()) }
                showUnreadBadge -> Badge()
            }
        }
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
        )
    }
}

