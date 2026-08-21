package com.galeria.medicationstracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.galeria.medicationstracker.navigation.MainNavHost
import com.galeria.medicationstracker.ui.HeadViewModel
import com.galeria.medicationstracker.ui.componentsOld.bottomNavItems
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.utils.navigation.GraphRoutes
import com.google.android.gms.common.util.CollectionUtils.listOf
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HeadActivity : ComponentActivity() {

  private lateinit var auth: FirebaseAuth
  private val startDestinations =
      listOf(GraphRoutes.Auth, GraphRoutes.Home)
  private var currentDestination: GraphRoutes = startDestinations[0]
  private val headViewModel: HeadViewModel by viewModels()

  override fun onStart() {
    super.onStart()

    auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    currentDestination = if (currentUser != null) {
      startDestinations[1]
    } else {
      startDestinations[0]
    }
  }

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      enableEdgeToEdge()
      val navController = rememberNavController()
      MedTrackerTheme {
        // val uiState by medicationsViewModel.uiState.collectAsStateWithLifecycle()

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        ObserveAsEvents(flow = SnackbarController.events, snackbarHostState) { event ->
          scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            val result =
                snackbarHostState.showSnackbar(
                  message = event.message,
                  actionLabel = event.action?.name,
                  duration = SnackbarDuration.Short,
                )

            if (result == SnackbarResult.ActionPerformed) {
              event.action?.action?.invoke()
            }
          }
        }
        val items = bottomNavItems()
        Log.d("Routes: ", items.toString())
        MainNavHost()
        /*     Scaffold(
               snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
               modifier = Modifier.windowInsetsPadding(WindowInsets.displayCutout),
               containerColor = MedTrackerTheme.colors.secondaryBackground,
               bottomBar = {
                 val navBackStackEntry by navController.currentBackStackEntryAsState()
                 val currentDestination = navBackStackEntry?.destination?.route
                 Log.d("currentDestination", currentDestination.toString())
                 val routesOldWithoutBottomBar =
                     listOf(
                       AuthScreen.Login.route,
                       AuthScreen.Registration.route,
                       AuthScreen.PasswordRecovery.route,
                     )

                 if (currentDestination !in routesOldWithoutBottomBar) {
                   // Индекс текущего пункта меню.
                   val vmIndex = headViewModel.selectedItemIndex.collectAsState().value
                   Log.d("currentDestination", currentDestination.toString())
                   BottomNavBar(
                     items,
                     navController,
                     //headViewModel,
                     currentIndex = vmIndex,
                     onBottomNavItemClick = headViewModel::updateSelectedItemIndex
                   )
                 }
               },
             ) {
               MainNavHost(modifier = Modifier.padding(it))
               *//*   ApplicationNavHost(
               modifier = Modifier
                 .fillMaxSize()
                 .padding(it),
               navController = navController,
               startDestination = currentDestination,
             )*//*
        }*/
      }
    }
  }
}

@Composable
fun SnackbarHandler(snackbarHostState: SnackbarHostState) {
  val scope = rememberCoroutineScope()
  ObserveAsEvents(flow = SnackbarController.events, snackbarHostState) { event ->
    scope.launch {
      snackbarHostState.currentSnackbarData?.dismiss()
      val result =
          snackbarHostState.showSnackbar(
            message = event.message,
            actionLabel = event.action?.name,
            duration = SnackbarDuration.Short,
          )

      if (result == SnackbarResult.ActionPerformed) {
        event.action?.action?.invoke()
      }
    }
  }
}
