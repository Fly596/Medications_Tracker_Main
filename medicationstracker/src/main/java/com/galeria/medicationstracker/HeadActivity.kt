package com.galeria.medicationstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.galeria.medicationstracker.navigation.ApplicationNavHost
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeadActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            enableEdgeToEdge()
            MedTrackerTheme { ApplicationNavHost() }
        }
    }
}
