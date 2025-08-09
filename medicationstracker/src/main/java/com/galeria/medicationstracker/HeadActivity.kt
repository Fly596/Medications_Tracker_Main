package com.galeria.medicationstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.utils.navigation.ApplicationNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HeadActivity : ComponentActivity() {
    /*     private lateinit var auth: FirebaseAuth
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
        } */
    
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            enableEdgeToEdge()
            // val navController = rememberNavController()
            MedTrackerTheme {
                ApplicationNavHost()
                
                // val uiState by medicationsViewModel.uiState.collectAsStateWithLifecycle()
                /*val snackbarHostState = remember { SnackbarHostState() }
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
                val items = bottomNavItems()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                // Текущий маршрут.
                val curRoute = navBackStackEntry?.destination?.route
                    ?: BottomNavigation.DASHBOARD.route::class.qualifiedName.orEmpty()
                val currentRouteTrimmed by
                remember(curRoute) {
                    derivedStateOf { curRoute.substringAfter("?") }
                }
                val shouldShowBottomBar = BottomNavigation.entries.any {
                    it.route::class.qualifiedName == currentRouteTrimmed
                }
                Scaffold(
                     val currentRoute =
                         navBackStackEntry?.destination?.route ?: Scaffold(
                             snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                             modifier = Modifier.windowInsetsPadding(WindowInsets.displayCutout),
                             containerColor = MedTrackerTheme.colors.secondaryBackground,
                             bottomBar = {
                                 if (shouldShowBottomBar) {
                                     BottomNavBar(
                                         items,
                                         navController,
                                         headViewModel
                                     )
                                 }
                                   val currentDestination =
                                      navBackStackEntry?.destination?.route
                                  val s = navBackStackEntry?.id
                                  val routesWithoutBottomBar =
                                      listOf(
                                          AuthScreen.Login.toString(),
                                          AuthScreen.Registration.toString(),
                                          AuthScreen.PasswordRecovery.toString(),
                                      )
                                  val logScr = AuthScreen.Login
                                  
                                  if (currentDestination !in routesWithoutBottomBar) {
                                      BottomNavBar(
                                          items,
                                          navController,
                                          headViewModel
                                      )
                                  }
                             },
                ) {
                    ApplicationNavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        /* .padding(start = 16.dp, end = 16.dp, top = 16.dp) */
                        navController = navController,
                        startDestination = currentDestination,
                    )
                }*/
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
