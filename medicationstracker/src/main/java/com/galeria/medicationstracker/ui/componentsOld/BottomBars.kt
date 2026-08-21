package com.galeria.medicationstracker.ui.componentsOld

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.utils.navigation.GraphRoutes
import com.galeria.medicationstracker.utils.navigation.GraphRoutes.Auth.route

data class BottomNavItem(
  val title: String,
  val route: GraphRoutes,
  val selectedIcon: Int,
  val unselectedIcon: Int,
  val hasNews: Boolean = false,
  val badgeCount: Int? = null,
)

fun bottomNavItems(): List<BottomNavItem> {
  return listOf(
    BottomNavItem(
      title = "Dashboard",
      route = GraphRoutes.Home,
      selectedIcon = R.drawable.home_fill,
      unselectedIcon = R.drawable.home,
    ),
    BottomNavItem(
      title = "Medications",
      route = GraphRoutes.Medications,
      selectedIcon = R.drawable.lab_profile_fill,
      unselectedIcon = R.drawable.lab_profile,
      hasNews = false,
    ),
    BottomNavItem(
      title = "Profile",
      route = GraphRoutes.PatientDashboard,
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
  //viewModel: HeadViewModel,
  currentIndex: Int = 0,
  onBottomNavItemClick: (Int) -> Unit = {},
) {

  Column {
    NavigationBar(
      // modifier = Modifier.fillMaxWidth(),
      containerColor = MedTrackerTheme.colors.primaryBackground,
      contentColor = MedTrackerTheme.colors.primaryLabel,
    ) {
      navItems.forEachIndexed { navItemIndex, navItem ->
        NavigationBarItem(
          selected = currentIndex == navItemIndex,
          colors =
              NavigationBarItemDefaults.colors(
                indicatorColor = MedTrackerTheme.colors.primaryTinted
              ),
          onClick = {
            onBottomNavItemClick(navItemIndex)
            try {
              navController.navigate(navItem.route)
            } catch (illegalArgumentException: IllegalArgumentException) {
              Log.e(
                "NavigationError",
                "Invalid route: $route, navigating to Home.",
                illegalArgumentException
              )
              navController.navigate(GraphRoutes.Home.route) {
                popUpTo(navController.graph.startDestinationId) { // Or your specific home route ID
                  saveState = true
                }
                launchSingleTop =
                    true // Avoid multiple copies of the home screen
                restoreState =
                    true    // Restore state if previously visited
              }
            }
          },
          label = {
            Text(text = navItem.title, style = MedTrackerTheme.typography.bodyMedium)
          },
          icon = {
            IconWithBadge(
              icon =
                  if (navItemIndex == currentIndex) navItem.selectedIcon
                  else navItem.unselectedIcon,
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
    Icon(painter = painterResource(id = icon), contentDescription = contentDescription)
  }
}
