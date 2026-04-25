package com.galeria.medtracker2

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.galeria.medtracker2.core.common.MainViewModel
import com.galeria.medtracker2.core.ui.theme.SpeechRecognitionAppTheme
import com.galeria.medtracker2.navigation.AppRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeechRecognitionAppTheme {
                val context = LocalContext.current
                // Check if permission granted.
                // TODO: запрашивать разрешение только после настройки первого приема.
                var hasNotificationPermission by remember {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS,
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                }
                val permissionLauncher =
                        rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.RequestPermission(),
                            onResult = { isGranted -> hasNotificationPermission = isGranted },
                        )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(
                        onClick = {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    ) {
                        Text(text = "Request the permission")
                    }
                    Button(
                        onClick = {
                            if (hasNotificationPermission) {
                                showNotification()
                            }
                        }
                    ) {
                        Text(text = "Show notification")
                    }
                }
                // AppNavHost()
            }
        }
    }

    private fun showNotification() {
        val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification =
                NotificationCompat.Builder(applicationContext, "channel_id")
                    .setContentText("Time to get paper")
                    .setContentTitle("Hello Broke Nigga")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .build()
        notificationManager.notify(1, notification)
    }
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(modifier = modifier, navController = navController, startDestination = AppRoutes.Home) {
        composable<AppRoutes.Home> {}
    }
}

@Composable
fun Greeting(
    name: String = "",
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val postNotificationsResultLauncher =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = {
                    viewModel.onPermissionResult(
                        permission = Manifest.permission.POST_NOTIFICATIONS,
                        isGranted = it,
                    )
                },
            )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {
                postNotificationsResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        ) {
            Text(text = "Request the permission")
        }
    }
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpeechRecognitionAppTheme { Greeting("Android") }
}
