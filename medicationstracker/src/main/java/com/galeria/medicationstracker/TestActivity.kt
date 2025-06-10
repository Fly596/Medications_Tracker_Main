package com.galeria.medicationstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.galeria.medicationstracker.ui.theme.SpeechRecognitionAppTheme

class TestActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeechRecognitionAppTheme {
                TestMedicationDataScreen()
            }
        }
    }
}
