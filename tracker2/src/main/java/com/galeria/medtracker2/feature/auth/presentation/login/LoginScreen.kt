package com.galeria.medtracker2.feature.auth.presentation.login

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.galeria.medtracker2.R
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.core.ui.components.MySwitch

@Composable fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {}

@Composable
fun RememberMeSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            stringResource(R.string.show_password),
            style = MedTrackerTheme.typography.bodyMedium
        )
        
        MySwitch(checked = checked, onCheckedChange = onCheckedChange)
    }
}