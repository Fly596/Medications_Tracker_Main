package com.galeria.medtracker2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.galeria.medtracker2.core.notifications.ReminderNotification
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MedTrackerTheme { AppNavHost() } }
    }

    private fun showNotification() {
        val remNot = ReminderNotification(this)
        remNot.showNotification("Notification")
    }
}
