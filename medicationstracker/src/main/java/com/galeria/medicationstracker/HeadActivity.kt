package com.galeria.medicationstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.galeria.medicationstracker.ui.HeadViewModel
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.utils.navigation.ApplicationNavHost
import com.galeria.medicationstracker.utils.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HeadActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private val startDestinations = listOf(
        Routes.NavigationRoutes.AUTH,
        Routes.NavigationRoutes.PATIENT_DASHBOARD,
    )
    private var currentDestination: String = startDestinations.get(0)

    private val headViewModel: HeadViewModel by viewModels()

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentDestination = startDestinations.get(1)
        } else {
            currentDestination = startDestinations.get(0)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            val navController = rememberNavController()
            MedTrackerTheme {
                val snackbarHostState = remember {
                    SnackbarHostState()
                }
                val scope = rememberCoroutineScope()
                ObserveAsEvents(flow = SnackbarController.events, snackbarHostState) { event ->
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        val result = snackbarHostState.showSnackbar(
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

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState
                        )
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.displayCutout),
                    containerColor = MedTrackerTheme.colors.secondaryBackground,

                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination?.route
                        val routesWithoutBottomBar = listOf(
                            Routes.NavigationRoutes.LOGIN,
                            Routes.NavigationRoutes.REGISTRATION,
                            Routes.NavigationRoutes.PASSWORD_RECOVERY,
                            Routes.NavigationRoutes.DOC_DASHBOARD,
                            Routes.NavigationRoutes.DOC_PATIENTS_LIST
                        )

                        if (currentDestination !in routesWithoutBottomBar) {
                            BottomNavBar(items, navController, headViewModel)

                        }
                    },

                    ) {
                    ApplicationNavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                            .padding(horizontal = 16.dp),
                        navController = navController,
                        startDestination = currentDestination
                    )
                }
            }
        }
    }

}

@Composable
fun SnackbarHandler(snackbarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()
    ObserveAsEvents(
        flow = SnackbarController.events,
        snackbarHostState
    ) { event ->
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