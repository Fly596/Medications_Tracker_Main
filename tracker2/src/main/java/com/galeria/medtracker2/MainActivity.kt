package com.galeria.medtracker2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.galeria.medtracker2.core.notification.ReminderNotification
import com.galeria.medtracker2.core.ui.theme.SpeechRecognitionAppTheme
import com.galeria.medtracker2.navigation.AppNavHost
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
                AppNavHost()
                /*             Column(
                                 modifier = Modifier.fillMaxSize().padding(top = 64.dp),
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

                             }*/
                // AppNavHost()
            }
        }
    }

    private fun showNotification() {
        val remNot = ReminderNotification(this)
        remNot.showNotification("Notification")

    }
}

